
先新曾UserInfo表 五位使用者
插入 order 表 2000筆資料

```sql
-- 先清空資料（可選）
TRUNCATE TABLE Orders;

-- 插入 2000 筆訂單資料
DELIMITER $$

CREATE PROCEDURE insert_fake_orders()
BEGIN
  DECLARE i INT DEFAULT 0;

  WHILE i < 2000 DO
    INSERT INTO Orders (
      user_id,
      shipping_method,
      payment_method,
      subtotal,
      freight,
      total,
      status_id,
      recipient_name,
      recipient_phone,
      recipient_email,
      recipient_address,
      recipient_time
    )
    VALUES (
      FLOOR(1 + RAND() * 5),                   -- user_id: 1~5
      'delivery',                              -- shipping_method
      'credit_card',                           -- payment_method
      FLOOR(100 + RAND() * 900),               -- subtotal: 100~999
      60,                                      -- freight
      FLOOR(160 + RAND() * 940),               -- total: 160~1099
      2,                                       -- status_id: paid
      CONCAT('User', i),                       -- recipient_name
      '0912345678',                            -- recipient_phone
      CONCAT('user', i, '@example.com'),       -- recipient_email
      CONCAT('Address ', i),                   -- recipient_address
      'anytime'                                -- recipient_time
    );

    SET i = i + 1;
  END WHILE;
END$$

DELIMITER ;

-- 執行插入程序
CALL insert_fake_orders();

-- 移除程序（清潔收尾）
DROP PROCEDURE insert_fake_orders;
```
```txt
1. 在 Order 表 插入 5000 筆資料
2. 用 sql "SELECT userId, totoal FROM Orders WHERE userId = ?" 一筆筆把資料拿出來
3. 拿出一筆資料後，建立一個 job(uuid/userid/total) 並放入 queue
4. queue 用 redis 來實作(語法再查一下
5. 起另一個spring boot 專案用來接收 queue 裡面的資料
6. 接收到資料後，把每個user的資料存到redis裡面，用userId當作key,做計算器去加總
7. 等到queue裡面的資料都拿完之後，每個使用者的資料都算完之後，到redis裡面去拿資料，把資料送回給使用者(想一下怎麼做)
```
```txt
一、資料庫設計與產生測試資料
------------------------------------------
新增資料表
CREATE TABLE OrderReport (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  userId    BIGINT       NOT NULL,
  total     BIGINT       NOT NULL,
  status    VARCHAR(20)  NOT NULL DEFAULT 'NEW',  -- 可追蹤訂單狀態
  createdAt DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_userId (userId)
);
------------------------------------------
TRUNCATE TABLE OrderReport;

新增5000筆資料
SET SESSION cte_max_recursion_depth = 5000;

INSERT INTO OrderReport (userId, total)
SELECT
  FLOOR(RAND() * 5) + 1 AS userId,
  FLOOR(RAND() * 901) + 100 AS total
FROM (
  WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 5000
  )
  SELECT n FROM seq
) AS tmp;
------------------------------------------

二、Producer（生產者）流程：初步併入 Queue 的時機與方式
你的原本想法是「每查到一筆，就立刻塞一個 Job」。這太浪費，也容易出問題。正確做法如下──

Step 1：從 Orders 拉出待處理訂單（只拉狀態為 NEW 的）
SELECT id, userId, total
FROM Orders
WHERE status = 'NEW';
只查出 status = 'NEW' 的訂單，避免重複推送。

這裡一次查完所有資料，不要分 userId 一條條查。一次 SELECT 全拿完再後續拆成 Job 才符合「一次性批次推送」或「批次切分」的需求。

Step 2：為這批待處理訂單分批（Batch Size）產生 Job

不建議單筆 Job：一筆一筆推太慢，5000 筆就是 5000 次 LPUSH/RPUSH，浪費連線。

建議批次推送：每 N 筆（例如 100 筆）包成一個 Job。比如 batchSize = 100，就會產生 50 個 Job；最後一批可能 <100 筆。
------------------------------------------
Job 格式 JSON 內容範例：

jsonc
複製
編輯
{
  "jobId": "550e8400-e29b-41d4-a716-446655440000",  // UUID
  "orderIds": [ 101, 102, 103, … 200 ],
  "type": "ORDER_AGGREGATION_BATCH"  // 方便未來擴充型別
}
為什麼要帶 orderIds 而不是只 userId？：

你必須防止「漏掉哪筆訂單」或「重複計算哪筆訂單」。

orderIds 除了能讓 Worker 知道要處理哪些訂單，也能在處理完成後，馬上把 status 更新成 PROCESSING，最後變成 DONE 或 FAILED。

Step 3：把每個批次 Job 推到 Redis Queue

選擇 Redis List 作為最簡單的 FIFO 隊列。

使用命令：

bash
複製
編輯
LPUSH order_jobs_queue '{"jobId":"550e8400-...", "orderIds":[…]}' 
或者如果希望先進先出，可以用 RPUSH，Worker 用 BLPOP 拿：

bash
複製
編輯
RPUSH order_jobs_queue '{"jobId":"550e8400-...", "orderIds":[…]}'
**注意：**千萬不要直接用 SET 或 HSET，那都不是 FIFO 工作隊列。

Step 4：將對應訂單狀態改成 QUEUED

批次推送完成後，立刻執行：

sql
複製
編輯
UPDATE Orders
SET status = 'QUEUED'
WHERE id IN (101, 102, …, 200);
這樣日後重跑也知道「哪些訂單已經在 Queue 裡，避免重複推」。

**務必在同一個 Transaction 裡做這兩件事（推送 Redis + 更新 DB），**否則出現極端情況：你的 Spring 程式送進 Redis 但未更新 DB，下一次又推一次，就重複。

三、Queue（Redis）結構與機制
使用最基礎的 List：order_jobs_queue

命令：

推入隊列（生產者用）
bash
複製
編輯
RPUSH order_jobs_queue '{"jobId":"...", "orderIds":[…]}'
取出隊列（消費者用，阻塞版）
bash
複製
編輯
BLPOP order_jobs_queue 0
0 表示「永不超時，一直等到有資料才回」。

加入 Processing Queue 保險機制（防止 Job 消失）
如果 Consumer 在拿出 Job 時程序崩潰，Job 就會消失。必須用 Reliable Queue 的做法：

Redis 有兩組 List：

order_jobs_queue：主 Queue

order_jobs_processing_queue：處理 Queue

Consumer 拿 Job 的流程：

使用 RPOPLPUSH order_jobs_queue order_jobs_processing_queue（原子指令）

取得返回的 Job JSON 字串

處理成功後：從 order_jobs_processing_queue 執行 LREM order_jobs_processing_queue 1 <jobJson>，移除這筆 Job 並不放回主 Queue

如果處理失敗或崩潰，因為 Job 還在 order_jobs_processing_queue，可以做定期掃描（監控）時，將「超過 X 秒（Visibility Timeout）」的 Job 重新投回 order_jobs_queue。用 BRPOPLPUSH 也行：

bash
複製
編輯
BRPOPLPUSH order_jobs_queue order_jobs_processing_queue 5  # 5 秒超時
這樣就確保 Job 不會被吞掉。

四、Consumer（Worker）邏輯與實作
單一 Spring Boot 專案＋Profile

嚴格禁止把 Producer、Consumer 分成兩個獨立專案：以後要改一邏輯你要改兩份 code，版本三不五時不同步，糟糕到爆。

正確做法：同一個 Spring Boot 專案，定義兩個 @SpringBootApplication 或在同一個 App 內加一個 @Component 標註的 Consumer Service，啟動時透過 --spring.profiles.active=consumer 或 --producer 來分工。

例如：

java
複製
編輯
// ProducerApplication.java
@SpringBootApplication
@Profile("producer")
public class ProducerApplication { 
  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }
}

// ConsumerApplication.java
@SpringBootApplication
@Profile("consumer")
public class ConsumerApplication { 
  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }
}
透過這招，你只要維護一套程式、一份 application.yml，啟動 Producer 時：

lua
複製
編輯
java -jar your-app.jar --spring.profiles.active=producer
啟動 Consumer 時：

lua
複製
編輯
java -jar your-app.jar --spring.profiles.active=consumer
Consumer 主要邏輯流程
假設我們使用 order_jobs_queue + order_jobs_processing_queue，消費者（Worker）要做的步驟如下：

連線 Redis（通常放在 Service 層的 bean 初始化時建好 JedisPool 或 Lettuce Connection Pool）

循環等待 & 拿 Job

java
複製
編輯
while (!Thread.currentThread().isInterrupted()) {
  // 用 BRPOPLPUSH：如果沒有 job 就阻塞等待
  String jobJson = jedis.brpoplpush("order_jobs_queue", "order_jobs_processing_queue", 0);
  if (jobJson == null) {
    continue;  // 不太可能，因為 0 秒代表永遠等
  }
  // 換成 Java Object
  Job job = objectMapper.readValue(jobJson, Job.class);
  try {
    processJob(job);
    // 成功就把它從 processing queue 刪掉
    jedis.lrem("order_jobs_processing_queue", 1, jobJson);
  } catch (Exception ex) {
    // 處理失敗，不要馬上丟回主 queue，等到「監控機制」偵測到超時再回。
    log.error("Job 處理失敗，保留在 processing queue 等待重試: " + job.getJobId(), ex);
  }
}
processJob(Job job) 方法內部要做：

java
複製
編輯
public void processJob(Job job) {
  List<Long> orderIds = job.getOrderIds();
  // 1. 對這批 orderIds 一次 SELECT 取出所有訂單資料
  List<Order> orders = orderRepository.findAllById(orderIds);

  // 2. 如果要驗證這些訂單確實存在，或有其他欄位檢查（例如 total > 0），就在此處做
  //    若發現哪筆訂單狀態不對或資料遺失，打 log 並標記為 FAILED
  if (orders.size() != orderIds.size()) {
    // 代表有訂單漏抓，要看是哪筆漏抓
    throw new RuntimeException("訂單數量不一致，可能有人手動刪單，jobId=" + job.getJobId());
  }

  // 3. 用 Map 聚合這批訂單的使用者金額
  Map<Long, Long> localAggregate = new HashMap<>();
  for (Order o : orders) {
    localAggregate.merge(o.getUserId(), o.getTotal(), Long::sum);
  }

  // 4. 使用 Redis 原子操作把每個使用者金額累計到全局匯總 Hash
  //    用 HINCRBY user_totals userId summedAmount
  for (Map.Entry<Long, Long> entry : localAggregate.entrySet()) {
    Long userId = entry.getKey();
    Long sumAmt = entry.getValue();
    jedis.hincrBy("user_totals", String.valueOf(userId), sumAmt);
  }

  // 5. 處理完這批訂單後，把對應 Orders 表的 status 更新為 DONE
  orderRepository.markOrdersAsDone(orderIds);
  //    也可以紀錄哪個 Job 處理過，或寫到一張 job_log 表去：jobId, status, processTime
}
注意 Atomic 操作

用 jedis.hincrBy("user_totals", userId, sumAmt)，絕對不能先 HGET 再 HSET。

如果多個 Consumer 同時作業，HINCRBY 保證 Redis 端原子加法，不會讓金額算錯。

Worker 崩潰或處理失敗補償機制

你已經把 Job 先移到 order_jobs_processing_queue，所以如果 Consumer 掛掉，Job 還在這個 List 裡。

定時清理程式（Watchdog）：你必須寫一個定時任務，例如每隔 N 秒（建議 N=30 秒）檢查 order_jobs_processing_queue。做法：

用 LRANGE order_jobs_processing_queue 0 -1 一律取出所有待處理 Job JSON 字串。

針對每個 Job，檢查你的 job_log 表裡，該 Job 最後更新的時間。如果距上次更新 > 一定閾值（例如 60 秒）且仍未標記 DONE，就代表上一次 Consumer 可能 crash 或執行失敗。

將該 Job JSON 復原到主 queue：

bash
複製
編輯
RPUSH order_jobs_queue <jobJson>
LREM order_jobs_processing_queue 1 <jobJson>
同時在 job_log 裡標記 RETRIED_ONCE = true，避免同一個 Job 無限次重試。超過重試次數（例如 3 次）後就標記 FAILED，不要再丟了，而是要人工介入。

五、Producer API 設計：/api/1.0/report/payments 與 /api/2.0/report/payments
API 1.0：同步版本（不用 Queue）

路由：GET /api/1.0/report/payments

功能：直接從 DB 一次撈 SELECT userId, total FROM Orders;，在 Java 裡用 Map 聚合，最後回傳 JSON。

實作範例（Spring Boot + JPA 或 JdbcTemplate）：

java
複製
編輯
@RestController
@RequestMapping("/api/1.0/report")
public class ReportControllerV1 {

  @Autowired
  private OrderRepository orderRepository;

  @GetMapping("/payments")
  public ResponseEntity<Map<Long, Long>> getPayments() {
    List<Order> allOrders = orderRepository.findAll();
    Map<Long, Long> aggregate = new HashMap<>();
    for (Order o : allOrders) {
      aggregate.merge(o.getUserId(), o.getTotal(), Long::sum);
    }
    // 包裝成前端需要的格式
    List<Map<String, Object>> data = new ArrayList<>();
    for (Map.Entry<Long, Long> entry : aggregate.entrySet()) {
      Map<String, Object> rec = new HashMap<>();
      rec.put("user_id", entry.getKey());
      rec.put("total_payment", entry.getValue());
      data.add(rec);
    }
    Map<String, Object> result = new HashMap<>();
    result.put("data", data);
    return ResponseEntity.ok(result);
  }
}
缺點：如果 Orders 數量暴增到上百萬筆，單次拉回來就會吃爆記憶體。你只能用 Page 分批，也會拖慢回應速度。此版本不符合「用 Queue 以提升使用者體驗」的需求，只是參考用。

API 2.0：非同步版本（Queue 驅動）

路由：POST /api/2.0/report/payments

步驟：

前端呼叫 POST /api/2.0/report/payments，不需任何參數（或可加日期範圍做區分）。

Controller 端：

生成一個唯一 jobId（例如 UUID.randomUUID().toString()）。

先 SELECT id, userId, total FROM Orders WHERE status = 'NEW'; 拿到所有「還沒丟 Queue」的訂單清單。

如果查出來的結果為空，直接回：

json
複製
編輯
{ "jobId": "<jobId>", "message": "沒有需要處理的訂單", "status": "EMPTY" }
否則，依照「三.2 Step 2」分批（batchSize = 100）把所有訂單拆成多個 Job JSON。

針對每個 Job：

把 Job JSON RPUSH order_jobs_queue。

批量更新 Orders SET status='QUEUED'。務必用同一個 Transaction 做：

java
複製
編輯
@Transactional
public void pushBatchToQueue(List<Job> jobs) {
  for (Job job : jobs) {
    jedis.rpush("order_jobs_queue", objectMapper.writeValueAsString(job));
  }
  orderRepository.updateStatusForOrders(job.getOrderIds(), "QUEUED");
}
Job 全部推完後，回應：

json
複製
編輯
{
  "jobId": "<jobId>",
  "message": "Jobs 已成功提交至 Queue",
  "status": "QUEUED"
}
Response 範例：HTTP 201 Created

json
複製
編輯
{
  "jobId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "Jobs 已成功提交至 Queue",
  "status": "QUEUED"
}
重要：不要試圖在這裡「等待 Consumer 處理完所有 jobs 再回」。那就失去非同步的意義。必須馬上回 QUEUED，讓前端去查後續結果。

查詢 Job 狀態與結果

路由：GET /api/2.0/report/payments/{jobId}

功能：讓前端知道這個 jobId 到底是「還在處理中」「已完成」「失敗」，以及若完成要回傳完整的 data: [ { user_id, total_payment }, … ]。

實作說明：

當 Consumer 處理完最後一個 Batch Job 時，要把「最終結果」寫到：

Redis Hash job_results，Key=jobId，Value=JSON.stringify({ status:"DONE", data:[…] })；

並且在 DB 新增一筆到 job_log 表：記錄 jobId、完成時間、處理使用時間。

如果 Consumer 某個 Batch 處理失敗且已達重試上限，就將 job_results 裡面的 Key=jobId 的 Value 改成 JSON.stringify({ status:"FAILED", reason:"…失敗原因…" })。

Controller 收到查詢請求，先去 Redis 讀：

java
複製
編輯
String jobResultJson = jedis.hget("job_results", jobId);
if (jobResultJson == null) {
  // 還沒開始，回 processing
  return ResponseEntity.ok(Map.of("jobId", jobId, "status", "PROCESSING"));
}
// 如果有值，就直接回
Map<String, Object> resultMap = objectMapper.readValue(jobResultJson, new TypeReference<>(){});
return ResponseEntity.ok(resultMap);
Response 範例（處理中）

json
複製
編輯
{ "jobId": "550e8400-...", "status": "PROCESSING" }
（成功完成）

json
複製
編輯
{
  "jobId": "550e8400-...",
  "status": "DONE",
  "data": [
    { "user_id": 1, "total_payment": 6380 },
    { "user_id": 2, "total_payment": 1250 },
    … 
  ]
}
（失敗）

json
複製
編輯
{
  "jobId": "550e8400-...",
  "status": "FAILED",
  "reason": "Batch 處理失敗，超過重試次數"
}
六、Consumer 處理完之後「最終結果」如何產生
分階段產生局部匯總
在 Consumer 處理每個 Batch Job 時，已經用 HINCRBY user_totals 原子累加。這會累計所有 batch 裡各個 userId 的金額。

Hash Key：user_totals

Field：String.valueOf(userId)

Value：Running total（金額）

何時寫到 job_results？

因為一次 Job 被拆成多個 Batch，只有最後一個 Batch 處理完時，才能把「完整最終匯總」寫到 job_results。

判斷方式：job_log 裡面有一張表紀錄：

sql
複製
編輯
CREATE TABLE job_log (
  jobId        VARCHAR(36) PRIMARY KEY,
  totalBatches INT        NOT NULL,
  doneBatches  INT        NOT NULL DEFAULT 0,
  createdAt    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updatedAt    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  status       VARCHAR(20) NOT NULL DEFAULT 'PROCESSING'
);
Consumer 處理每個 Batch 時要做：

處理完 batch 裡的所有訂單累加後：

java
複製
編輯
// 取出 job_log
JobLog log = jobLogRepository.findById(job.getJobId());
log.setDoneBatches(log.getDoneBatches() + 1);
jobLogRepository.save(log);
檢查 doneBatches == totalBatches：

如果「還沒到」，就結束該 Batch，不要寫 result。

如果「等於」，代表這是最後一批，現在可以把最終結果從 user_totals 拉回來，寫到 job_results：

java
複製
編輯
Map<String, String> allTotals = jedis.hgetAll("user_totals");
// 將 allTotals 轉成 List<Map<String, Object>> data
// 然後塞到 job_results
jedis.hset("job_results", job.getJobId(), objectMapper.writeValueAsString(Map.of(
  "jobId", job.getJobId(),
  "status", "DONE",
  "data", dataList
)));
// 並且把 job_log.status 設為 DONE
log.setStatus("DONE");
jobLogRepository.save(log);
注意清理：

如果不清掉 user_totals 裡的資料，下次再跑另一個 job，會跟「上次結果疊加」出錯。必須在寫完 job_results 後，針對這個 job 做一次「清除操作」：

bash
複製
編輯
DEL user_totals
或者把 user_totals 設計成 key 包含 jobId，例如 user_totals:{jobId}， Consumer 處理完後直接刪掉整個 user_totals:{jobId}。

關鍵：一定要在最後把 Hash 整個取完、刪掉，否則資料會留在 Redis。下次有新 job 跑進來，這些值還在，結果會疊加錯亂。

七、錯誤重試與死信機制
JobLog 必須紀錄重試次數

在 job_log 表加一個 retryCount INT DEFAULT 0 欄位。

當 Consumer 處理某個 Batch 時發生 Exception，就先 retryCount += 1，如果 retryCount < MAX_RETRIES (例如 3)，就把該 jobId 的 status 改成 RETRYING，並將整個 Batch JSON 直接推回 order_jobs_queue。

如果 retryCount >= MAX_RETRIES，就把 status 標為 FAILED，並在 job_results 寫入失敗訊息。

Dead Letter Queue（死信佇列）

你要維護一個 Redis List：order_jobs_dead_letter_queue，如果某 Batch 超過重試次數，就把它 RPUSH order_jobs_dead_letter_queue。

同時在 job_log 做 status='FAILED' 記錄。前端查到 status=FAILED，就知道這個 job 完全失敗，需要人工介入。

自動重試掃描（Watchdog）

除了在同一個 Consumer 裡做 retry 邏輯，你還需要一個定時掃描 order_jobs_processing_queue 的程式。

每隔 30 秒：

LRANGE order_jobs_processing_queue 0 -1 取得所有 Batch JSON。

針對每筆，jobLog = jobLogRepository.findById(jobId)，如果 jobLog.getUpdatedAt() 距今 > 60 秒且 status != FAILED/DONE，表示它已經卡死。

如果 retryCount < MAX_RETRIES，就 RPUSH order_jobs_queue 該 Batch，LREM order_jobs_processing_queue 1 <jobJson>，並把 retryCount+1 更新、status='RETRYING'。

否則直接 LREM order_jobs_processing_queue 1 <jobJson>，RPUSH order_jobs_dead_letter_queue，jobLog.setStatus("FAILED")。

八、前端／API 客戶端如何整合
呼叫非同步 API

前端收到：

json
複製
編輯
{ "jobId": "550e8400-...", "message": "Jobs 已成功提交至 Queue", "status": "QUEUED" }
立刻跳到「查詢進度」畫面，或顯示「統計報表製作中」的 Spinner。

輪詢或訂閱

假設用最簡單的方式：每 5 秒呼叫一次：

bash
複製
編輯
GET /api/2.0/report/payments/550e8400-...
從 Response 判斷：

status=PROCESSING → 繼續輪詢。

status=DONE → 拿到 data，把報表顯示給使用者，結束輪詢。

status=FAILED → 顯示「統計失敗」訊息，並可引導使用者聯繫後端人員。

WebSocket / SSE 選項（進階）

如果你想更爽快，Consumer 在最後寫完 job_results 後可以同時「發一個 WebSocket 訊息」給前端，或者用 SSE push。那前端就不用一直輪詢。

實作上要先在後端開一個 WebSocket Server，前端在打 /api/2.0/report/payments 時，把 jobId 也綁定到一個 Channel（例如 "/topic/report/" + jobId）。Consumer 看到 job_results 寫入後就往 "/topic/report/" + jobId 發一條 JSON，前端收到後停止輪詢、載入結果。

九、Redis Pub/Sub vs. Queue 差異（選擇理由說明）
Redis Pub/Sub

特性：

訂閱者只要連上 channel，就能拿到發佈者推送的訊息。

不保留訊息，客戶端不在線就收不到（無法追溯歷史）。

多個 subscriber 都會拿到相同訊息，Broadcast 型。

適合場合：

要做「即時通知」→ 例如聊天、即時公告、線上遊戲狀態同步。

不是儲存工作任務，只要瞬間丟給所有訂閱者即可。

Redis List (Queue)

特性：

以 FIFO 方式存放工作任務。

只有一個消費者拿到某條訊息後，該訊息就從隊列消失。適合「工作分配」。

可以做 Reliable Queue + Dead Letter 等機制。

適合場合：

待處理工作量（Jobs）的分派、排隊等待，尤其需要「可靠性」（處理失敗要重推）。

本題「訂單批次匯總」屬於「工作分配」而非「廣播通知」，必須用 Queue。

為什麼不直接用 Pub/Sub？

如果你改用 PUBLISH order_jobs_queue <jobJson>：

消費者若當下沒訂閱（還沒啟動），那 Job 消失，訂單就不會被處理。

訂單可能被多個 Consumer 拿到（Pub/Sub 沒有「一個取走就沒了」的概念），就會重複累加金額。

因此本題一定要用 List（Queue），並搭配 Processing Queue 來實現可靠消費。

十、部署與注意事項
部署同一個 Jar，兩個 Profile

Producer：java -jar app.jar --spring.profiles.active=producer

Consumer：java -jar app.jar --spring.profiles.active=consumer

推薦方式：使用 Docker 或 Systemd 管理。比如：

dockerfile
複製
編輯
FROM openjdk:17-jdk-alpine
COPY app.jar /app/app.jar
CMD ["java","-jar","/app/app.jar","--spring.profiles.active=consumer"]
你可依需求改成 --spring.profiles.active=producer。

Redis 連線

拿到 Redis host/port 後，配置在 application-{profile}.yml：

yaml
複製
編輯
spring:
  redis:
    host: localhost
    port: 6379
保證連不上 Redis 時，程式不會在啟動就炸，要做重試與待連邏輯。

MySQL 連線設定

也須在 application.yml 中設定 datasource，並做連線池（HikariCP）最佳化，避免同時執行大量查詢時卡死。

Producer 一次查 5000 筆沒問題，但如果資料量爆到 10 萬筆，要考慮分頁機制或 Index。

監控與日誌

Consumer 的日誌必須詳細寫入 jobId、batchId、orderIds、執行時間、錯誤原因，便於排查。

推薦用 ELK（ElasticSearch + Logstash + Kibana）或 Grafana 搭配 Prometheus 去監控 Consumer 是否有異常滯留。例如：

order_jobs_processing_queue 長度一直不降

job_log 某些 job 狀態一直卡在 RETRYING

資安與錯誤訊息封裝

不可以把 Exception StackTrace 原樣回給前端。只回 status=FAILED + reason="系統異常，請聯繫管理員"。日誌裡再打印完整 stack。

清理殘留資料

Consumer 成功把最終結果寫到 job_results 並刪掉 user_totals:jobId 之後，記得找出所有鍵值對並刪除，以免 Redis 快取爆掉。

如果 order_jobs_dead_letter_queue 裡面長時間有訊息，要安排人工清理或搭配警告機制，避免佇列一直上升。

十一、完整作業流程總結
以下是你要照實執行的最終作業流程（絕對不要再漏任何細節）──

準備階段：
1.1. 建立 MySQL Orders、job_log 表。
1.2. 規劃 Redis 結構：
- List：order_jobs_queue、order_jobs_processing_queue、order_jobs_dead_letter_queue
- Hash：user_totals:{jobId}（或全域 user_totals，但要在每次 job 完成後清除）
- Hash：job_results

生成測試資料：
2.1. 執行 TRUNCATE TABLE Orders;。
2.2. 執行批次插入 5000 筆隨機訂單（如前面 SQL 或 Node.js 腳本）。

啟動 Producer（Spring Boot with profile=producer）：
3.1. 在 Controller 處理 POST /api/2.0/report/payments：
- 產生 jobId=UUID。
- 查 Orders WHERE status='NEW'，得到所有訂單清單 orderList。
- 如果 orderList.isEmpty() → 回 { jobId, status:'EMPTY', message: '無待處理訂單' }，結束。
- 否則，計算 totalBatches = ceil(orderList.size() / batchSize)， batchSize=100。
- 呼叫 job_log 儲存一筆：jobId, totalBatches, doneBatches=0, status='PROCESSING'。
- 依照每 batchSize 分拆 jobList，對於每個 job：
1. JSON.stringify → jobJson。（內容：jobId + orderIds 列表 + batchIndex + totalBatches）
2. 用 jedis.rpush("order_jobs_queue", jobJson) 推進主 Queue。
3. 將對應的 Orders.status 一次性更新成 QUEUED。
- 回應 { jobId, status:'QUEUED', message:'Jobs 已成功提交' }，HTTP 201。

啟動 Consumer（Spring Boot with profile=consumer）：
4.1. 一個獨立 Thread (或 @Scheduled) 不停阻塞執行：
java while (true) { String jobJson = jedis.brpoplpush("order_jobs_queue", "order_jobs_processing_queue", 0); Job job = objectMapper.readValue(jobJson, Job.class); processBatchJob(job, jobJson); // 之後細分 }
4.2. processBatchJob(Job job, String jobJson)：
1. SELECT id, userId, total FROM Orders WHERE id IN (:orderIds) → List<Order> orders。
2. 驗證：if (orders.size() != job.orderIds.size()) throw new RuntimeException("訂單不一致");
3. 用 Map<Long, Long> localAgg 聚合這批訂單金額：for (Order o : orders) localAgg.merge(o.userId, o.total, Long::sum);
4. 原子更新 Redis：
java String userTotalsKey = "user_totals:" + job.getJobId(); for (Map.Entry<Long, Long> e : localAgg.entrySet()) { jedis.hincrBy(userTotalsKey, String.valueOf(e.getKey()), e.getValue()); }
5. 更新 DB：UPDATE Orders SET status='DONE' WHERE id IN (:orderIds)。
6. 更新 job_log 表：doneBatches += 1，updatedAt=NOW()。
7. 如果 doneBatches < totalBatches → 直接結束此 batch。
8. 如果 doneBatches == totalBatches → 讀取一次 Redis Hash user_totals:{jobId}：
java Map<String, String> all = jedis.hgetAll("user_totals:" + job.getJobId()); List<Map<String, Object>> dataList = new ArrayList<>(); for (Map.Entry<String, String> field : all.entrySet()) { dataList.add(Map.of( "user_id", Long.parseLong(field.getKey()), "total_payment", Long.parseLong(field.getValue()) )); } // 寫入 job_results jedis.hset("job_results", job.getJobId(), objectMapper.writeValueAsString( Map.of("jobId", job.getJobId(), "status","DONE","data",dataList) )); // 更新 job_log.status = 'DONE' jobLog.setStatus("DONE"); jobLogRepository.save(jobLog); // 刪掉 Redis Hash jedis.del("user_totals:" + job.getJobId());
- 注意：一定要刪 Hash，否則下次 jobId 重複或資料殘留會錯亂。

4.3. 重試與 Dead Letter
- 合併 processBatchJob(...) 外層 try-catch：
java try { processBatchLogic(...); // 處理成功後，從 processing queue 移除 jedis.lrem("order_jobs_processing_queue", 1, jobJson); } catch (Exception ex) { // 先調升 retryCount JobLog log = jobLogRepository.findById(job.getJobId()); log.setRetryCount(log.getRetryCount() + 1); if (log.getRetryCount() < MAX_RETRIES) { // 設成 RETRYING，然後推回主 queue log.setStatus("RETRYING"); jobLogRepository.save(log); jedis.rpush("order_jobs_queue", jobJson); jedis.lrem("order_jobs_processing_queue", 1, jobJson); } else { // 超過次數，推到死信 queue, 標記 FAILED log.setStatus("FAILED"); jobLogRepository.save(log); jedis.rpush("order_jobs_dead_letter_queue", jobJson); jedis.lrem("order_jobs_processing_queue", 1, jobJson); // 同時寫 job_results jedis.hset("job_results", job.getJobId(), objectMapper.writeValueAsString( Map.of("jobId", job.getJobId(), "status","FAILED", "reason", ex.getMessage()) )); } }
- 定時掃描：
java @Scheduled(fixedDelay = 30000) public void requeueStuckJobs() { List<String> processingJobs = jedis.lrange("order_jobs_processing_queue", 0, -1); for (String jobJson : processingJobs) { Job job = objectMapper.readValue(jobJson, Job.class); JobLog log = jobLogRepository.findById(job.getJobId()); if (log.getStatus().equals("PROCESSING") && log.getUpdatedAt().isBefore(now.minusSeconds(60))) { // 超過 60 秒沒更新，視為卡死 if (log.getRetryCount() < MAX_RETRIES) { log.setRetryCount(log.getRetryCount() + 1); log.setStatus("RETRYING"); jobLogRepository.save(log); jedis.rpush("order_jobs_queue", jobJson); jedis.lrem("order_jobs_processing_queue", 1, jobJson); } else { log.setStatus("FAILED"); jobLogRepository.save(log); jedis.rpush("order_jobs_dead_letter_queue", jobJson); jedis.lrem("order_jobs_processing_queue", 1, jobJson); jedis.hset("job_results", job.getJobId(), objectMapper.writeValueAsString( Map.of("jobId", job.getJobId(), "status","FAILED", "reason","已超過重試次數") )); } } } }
- 這樣才能在 Consumer 崩潰或隱性錯誤時自動補償。

十二、總結檢查清單（Checklist）
在你最終提交作業之前，一定要逐條檢查：

 Orders 表格結構和 job_log 表建立沒錯。

 測試資料可以一次插入 5000 筆，並且可以多次 TRUNCATE 重跑。

 Redis List 命名必須正確：

order_jobs_queue

order_jobs_processing_queue

order_jobs_dead_letter_queue

 Redis Hash 使用：

user_totals:{jobId}（Batch 處理累加用）

job_results（放最終匯總或失敗原因）

 Producer API (POST /api/2.0/report/payments) 可以成功生成並回傳 jobId。

 job_log 第一筆寫入時要包含：jobId、totalBatches、doneBatches=0、status='PROCESSING'。

 BatchJob JSON 格式要包含：jobId、orderIds[]、batchIndex、totalBatches。

 Consumer 用 RPOPLPUSH 或 BRPOPLPUSH 從 order_jobs_queue 取出後，必須推到 order_jobs_processing_queue 對應。

 processBatchJob 邏輯要：

一次查出所有這批訂單

驗證筆數

聚合成 Map

用 HINCRBY user_totals:{jobId} 原子操作累加

更新 Orders.status='DONE'

更新 job_log.doneBatches +=1

若是最後一批 (doneBatches == totalBatches)，一次把 user_totals:{jobId} 全抓，存到 job_results，再刪掉 user_totals:{jobId}，把 job_log.status='DONE'

 Batch 處理若遇錯要：

把 retryCount +=1，且在 MAX_RETRIES 內 → status='RETRYING' 並 直接推回 order_jobs_queue，從 order_jobs_processing_queue 刪除。

超過 MAX_RETRIES → 推到 order_jobs_dead_letter_queue，job_log.status='FAILED'，同時寫 job_results 失敗訊息。

 定時掃描 Task 能把「卡死在 processing queue」的 Job 自動重推。

 提供查詢 API：GET /api/2.0/report/payments/{jobId} → 回「PROCESSING / DONE / FAILED」與對應資料。

 前端實作：收到 QUEUED 後要輪詢查詢狀態，或使用 WebSocket/SSE。

 不要把 Exception StackTrace 回給前端，後端要寫到 Log 裡，前端只顯示人性化錯誤。

 部署時確定有 Redis、MySQL、Producer、Consumer 各跑一份，並做好監控。

十三、附上範例程式碼片段（必要時照用）
Job 類別

java
複製
編輯
public class Job {
  private String jobId;
  private List<Long> orderIds;
  private int batchIndex;
  private int totalBatches;
  // getters & setters
}
JobLog 實體

java
複製
編輯
@Entity
@Table(name="job_log")
public class JobLog {
  @Id
  private String jobId;
  private int totalBatches;
  private int doneBatches;
  private int retryCount;
  private String status; // PROCESSING / RETRYING / DONE / FAILED
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  // getters & setters
}
OrderRepository（簡化版）

java
複製
編輯
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  @Modifying
  @Query("UPDATE Orders o SET o.status = :status WHERE o.id IN :orderIds")
  void updateStatusForOrders(@Param("orderIds") List<Long> orderIds, @Param("status") String status);
}
JobLogRepository（簡化版）

java
複製
編輯
@Repository
public interface JobLogRepository extends JpaRepository<JobLog, String> {
}
RedisConfig（Lettuce 範例）

java
複製
編輯
@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
    return factory;
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate() {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }
}
最後再提醒你一次：
只要有任何一個步驟做錯──比如用 HSET 代替 HINCRBY、或 Batch 切成 1 筆、或不更新 Orders.status，就一定壞掉。你必須嚴格按照上面的流程，才能保證「非同步批次匯總」能正確完成。若不完全理解上面每一步的意義，千萬不要動手寫，先搞懂再寫。```
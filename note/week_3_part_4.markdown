
先新增 UserInfo表 五位使用者
插入 order 表 2000筆資料

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

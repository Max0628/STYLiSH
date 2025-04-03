### DataBase Normalization

1NF 每個欄位只能有一個值
2NF 非主鍵不依賴部分主鍵(例如複合主鍵時，非主鍵可能依賴其中一個主鍵)
3NF 消除傳遞依賴，A 依賴 B，B 依賴 C，A 即是傳遞依賴 C
看到 spec JSON，先觀察資料之間的關係
一對一 / 一對多 / 多對多
先用 drawDB 畫出 table 關係圖
畫完後每張 table 逐一做正規劃拆表，拆到 3NF

```sql

CREATE TABLE `Product`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    category VARCHAR(50),
    title VARCHAR(255),
    description VARCHAR(255),
    price BIGINT,
    texture VARCHAR(255),
    wash VARCHAR(50),
    place VARCHAR(50),
    note VARCHAR(255),
    story VARCHAR(255),
    main_image_url VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE `Color`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(50),
    name VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE `Size`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    size VARCHAR(50),
    PRIMARY KEY(id)
);

CREATE TABLE `Image`(
    id BIGINT NOT NULL AUTO_INCREMENT,
    url VARCHAR(255),
    product_id BIGINT,
    PRIMARY KEY(id),
    FOREIGN KEY(product_id) REFERENCES Product(id)
);

CREATE TABLE `Variant`(
    size_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY(product_id,size_id,color_id),
    FOREIGN KEY(size_id) REFERENCES Size(id),
    FOREIGN KEY(color_id) REFERENCES Color(id),
    FOREIGN KEY(product_id) REFERENCES Product(id)
);
```
```sql
foreign key 開關
SET FOREIGN_KEY_CHECKS=0; 關閉
SET FOREIGN_KEY_CHECKS=1; 開啟
```
dump file manipulation
```sql
把目前db的資料放到dump檔案
mysqldump -u root -p --databases stylish > stylish.sql

把dump檔案的資料倒入到已經建立好的db
mysql -u root -p stylish < stylish.sql
```

設計完畢
學習到
PRIMARY KEY(product_id,size_id,color_id),這邊把最常用的 product_id 放到最左邊
因為 innodb 最左前綴原則（Leftmost Prefix Rule）
最左前綴原則（Leftmost Prefix Rule） 指的是 索引的查詢效率與查詢條件的「最左側」欄位匹配度有關


```txt
我的單一商品會有很多變體，使用者填入表單時，只會填寫變體，有數個變體
後端在插入資料庫時，會逐次插入每個變體，如果一個商品有4個變體abcd , 
此商品會先插入 商品的 product 噴出 product_id
把這個product_id存起來
----------
然後逐次插入 color_1 -> 得到 color1_id
然後逐次插入 size_1 -> 得到 size_1_id
然後逐次插入  -> 得到 size_1_id / color1_id / product_id
插入 variant_1_id
------------------------
然後逐次插入 color_2 -> 得到 color2_id
然後逐次插入 size_2 -> 得到 size_2_id
然後逐次插入  -> 得到 size_2_id / color2_id / product_id
插入 variant_2_id
```



### 圖片處理
```txt
1. 前端某些欄位，可能上傳一張圖片或是多張圖片
1. 儲存圖片url -> 存在 mysql string -> 預防重複 -> 使用 uuid or hash 避免重複？
2. 圖片本身(檔案命稱+二進位資料) -> 存在本地資料夾 or aws ec2本身 or s3 bucket 
3. url字串 domain 差異，在我筆電本地資料夾 or 在ec2 本身資料夾 or 在 s3 bucket 路徑不同？
4. 圖片超過一定大小不允許上傳？
5. 設定WebConfig來做到url跟資料夾的映射(url可以是http開頭，像是架了個本地image server)
6. 可以存在本地的資料夾 未來放到ec2的資料夾應該也可以用
7. mysql 資料庫應該只會儲存圖片的 uuid + 名稱
8. 圖片本身的位置，可能會放在本地，aws ec2 裡面的資料架，未來會放在 s3
9. 訪問的路徑，在本地 domain : http://localhost:8080, EC2: http://35.74.181.119, S3 bucket: ??
10.訪問的資料夾：
圖片訪問的路徑
picture access url :
url = domain + directory + images name( uuid + name )
網址是組出來的
mysql 裡面只會儲存 uuid + name

| location | domain                   | directory                | name                                                    |
| -------- | ------------------------ | ------------------------ | ------------------------------------------------------- |
| local    | http://localhost:8080    | /images                  | a3d2457b-ea4b-4620-85ba-55eddcf0054a_201902191247_0.jpg |
| Ec2      | http://35.74.181.119     | /home/ubuntu/image       | a3d2457b-ea4b-4620-85ba-55eddcf0054a_201902191247_0.jpg |
| S3       |                          |                          | a3d2457b-ea4b-4620-85ba-55eddcf0054a_201902191247_0.jpg |
```

### 錯誤處理
```txt
自定義 exception ，外面用全局處理器包住
```

### Service 邏輯
```txt
所以說我的 controller 應該只會負責接收 request 或是驗證等工作
我的servie才會去做 

在 controller 中，每次都要存入
1. 相同的 productForm 資料 (productForm)
2. 當前迭代的的 variant(variantForm) --> 會在 service 解析出 size / color
3. 當前迭代到的 images(imageForm)
4. 當前迭代到的 
```
```
1. 抓 productForm
2. 從 variant 解析出 sizeForm / colorForm 
3. 先插入 productForm -> 噴出 productId
4. 先插入 colorForm -> 噴出 colorId
5. 先插入 sizeForm -> 噴出 sizeId
6. 拿 productId / colorId / sizeId 當作 foreign key 插入 variantForm
7. 插入images (注意! images 跟 variant 無關哈哈哈哈)
```

```txt
controller 驗證請求是否正確，img 是否有欄位
service 解析圖片路徑，圖片名稱，圖片二進位資訊
```


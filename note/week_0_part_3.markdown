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

設計完畢
學習到
PRIMARY KEY(product_id,size_id,color_id),這邊把最常用的 product_id 放到最左邊
因為 innodb 最左前綴原則（Leftmost Prefix Rule）
最左前綴原則（Leftmost Prefix Rule） 指的是 索引的查詢效率與查詢條件的「最左側」欄位匹配度有關

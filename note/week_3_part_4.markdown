
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
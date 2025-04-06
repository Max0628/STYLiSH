### API ENDPOINT
```txt
swagger
http://localhost:8080/swagger-ui/index.html -> swagger 文件本地 dashboard
http://localhost:8080/v3/api-docs           -> swagger 文件打的 api (注意spring security 要打開)
---
ProductList
http://127.0.0.1:8080/api/v1/products/women?paging=2
http://127.0.0.1:8080/api/v1/products/men
http://127.0.0.1:8080/api/v1/products/accessories


```

### 商品頁碼取畫面邏輯
```sql
畫面每頁要顯示6筆商品資料
api 每次撈取7筆資料，如果撈資料成功(代表有下一頁，就可以回傳 next_page)
如果撈不到代表沒有下一頁了
因為使用者的畫面一次只會顯示1頁，一頁有6筆資料
所以打一次api就好了
如果使用者按下一頁，會再打一次api
LIMIT -> 指定回傳資料筆數
OFFSET-> 指定跳過前面 N 筆資料 從 N + 1  筆資料開始選取
ex:
SELECT * FROM `Product` 
WHERE category='men' 
LIMIT 7 //每次回傳七筆商品資料
OFFSET 6 //從 6 + 1 個商品開始計算 // 0, 6, 12, 18
```

### 拼出商品資料邏輯
```txt
1. 先找出 ProductId
2. 用 vroductId 找出相對應的 image -> 找出 image 資料
3. 用 productId 找出 相對應的 variantId -> 找出 variant 資料
4. 用 variantId 找出相對應的 colorId , SizeId -> 找出 color size 資料
```

### N + 1 問題
```txt
N+1 問題是一種常見的效能陷阱，主要出現在 ORM 操作關聯式資料表時。
若查詢一筆主表（如 Product）資料時，對每一筆資料再額外查一次關聯表（如 Variant），
就會產生 1 次主表查詢 + N 次關聯表查詢（共 N+1 次查詢）。

DB IO（資料庫 I/O） 是最貴的資源，尤其是大型系統。
CPU 記憶體計算 相對便宜，伺服器擴充性也好。
所以，IO-bound 的邏輯應盡可能少次數、多資料，在伺服器端處理。

參考資料：
https://stackoverflow.com/questions/97197/what-is-the-n1-selects-problem-in-orm-object-relational-mapping

```

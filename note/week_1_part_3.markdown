# 註冊與登入流程
### 測試環境

- 本地開發：https://127.0.0.1:8443
- 生產環境：http://35.74.181.119:8080

### API 文檔

#### 註冊

**端點：** `/api/v1/user/signup`

**請求體：**
```json
{
    "name": "joe",
    "email": "joe0628@gmail.com",
    "password": "Joe@@12345678"
}
```

#### 登入

**端點：** `/api/v1/user/signin`

**請求體：**
```json
{
  "provider": "native",
  "email": "joe0628@gmail.com",
  "password": "MJoe@@12345678"
}
```

#### 取得使用者資料

**端點：** `/api/v1/user/profile`

**請求頭：**
```
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ1NzQ4OTU0LCJleHAiOjE3NDU3NTI1NTR9.o0V7h26iMXlYmGE9z-S1gQGxJUVvV9Y7eJPW2TAq27q4LxFGQiFg3ppnL5nxiy8C
Content-Type: application/json
```
```


## 註冊 (Sign Up)

### 本地註冊流程
1. 前端輸入 username、email、password。
2. 後端接收 username、email、password。
3. 後端檢查 email 是否已經註冊過。
4. 驗證密碼格式是否符合安全規則（8 碼以上、大小寫、數字、符號各一）
5. 如果密碼格式正確，將 password 用 bcrypt hash。
6. 儲存 username、email、hashed password 到資料庫。
7. 從資料庫撈出 id、name、email、provider、picture。
8. 若未提供 picture，補上預設圖。
9. 使用 userId 為 payload，產出 JWT（程式庫產出）
10. 回傳 access_token、access_expired 與 user 給前端。
11. 前端收到後，將 JWT 存入 localStorage。


### Facebook 註冊流程
0. 前端先檢查 localStorage 裡面是否有 `access_token`，如果有就直接跳過 Facebook 登入流程。
1. 使用者點擊 Facebook 登入按鈕，觸發 Facebook SDK，跳出 Facebook 登入彈窗。
2. 使用者登入自己的 Facebook 帳號，得到 Facebook 的 `access_token`。
3. 拿 `access_token` 打 Facebook 的登入 API。
4. 後端拿 `access_token` 去打 Facebook 的 Graph API，取得使用者的資料（`name`、`email`、`picture`）。
5. 後端檢查 `email` 是否已經註冊過。
6. 如果尚未註冊過，把 Facebook 的資料存到資料庫（這邊不用存 `password`）。
7. 把 Facebook 的資料當作 JWT 的 payload。
8. 把 JWT header、payload 用 base64 encode。
9. 把 JWT header、payload 跟 secret 做 hash，產生 signature，然後把它放到 JWT 的第三段。
10. 得到完整的 JWT，開始準備回傳給前端的資料。
11. 把 JWT 放到 `access_token`，並且設定 `access_expired = 3600`。
12. 把資料庫撈出來的資料放到 user 裡面（`id`、`provider`、`name`、`email`、`picture`）。
13. 如果使用者沒有提供 `picture`，就用預設圖片，使用者登入後可以自行更換。
14. 把資料回傳給前端。
15. 前端把 JWT 存到 `localStorage`。

---

## 登入 (Sign In)

### 本地登入流程
0. 前端先檢查 localStorage 是否有 jwt，有的化就自動登入
1. 前端輸入 `email`、`password`。
2. 後端驗證 `email` 是否存在於資料庫。
3. 如果存在，檢查 `password` 是否正確。
4. 如果正確，產生 JWT。
5. 把資料庫的資料撈出來回傳給前端。
6. 把 JWT 放到 `access_token`，並且設定 `access_expired = 3600`。
7. 把 API 回傳給前端。

---

## JWT (JSON Web Token)

- 後端伺服器會接收前端使用者資料（如 `username`、`email` 等）。
- 用 hash 把資料當作 secret 放到 JWT 中，然後存放在使用者端（`cookie` 或 `localStorage`）。
- 與 `sessionId` 不同，`sessionId` 是存放在後端。
- JWT 本身是個鑰匙，像是園遊會的手圈，存放在使用者的瀏覽器中。

### 為什麼選用 JWT 並放在 `localStorage`？
- JWT 比較符合 stateless 的設計哲學。

### 延伸議題
- 資安：XSS / CSRF / CORS
- Token-based Auth vs. Session-based Auth
- Stateless 的意義與優勢
- `cookie` vs. `localStorage` vs. `sessionStorage`

---

## 資料庫設計

### User Table
```sql
CREATE TABLE `UserInfo` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    provider VARCHAR(50) NOT NULL, 
    picture VARCHAR(1024) DEFAULT NULL,
    PRIMARY KEY (id)
);
```
注意， password 可以是空的(因為fb登入)
圖片也可以是空的 (因為使用者沒有上傳)



# TapPay 結帳流程與關鍵名詞整理

## 整體流程
0. 設計好DB schema
1. 前端裝 TapPay SDK。
2. 使用者輸入卡號，TapPay SDK 在瀏覽器端檢查卡號格式正確性（卡號、到期日、安全碼），格式正確後，將卡資料送去 TapPay 換取 prime。
3. 前端拿 prime ＋ 商品/收件人/金額資料打後端 checkout API（帶上 access token (JWT)）。//現在做到這邊
4. 後端驗證 access token。
5. 後端從資料庫查商品：
    - 確認商品存在且未下架。
    - 確認庫存足夠。
    - 計算實際應支付金額。
    - 比對前端送來的金額是否正確。
6. 若無誤，新增一筆 unpaid 訂單。
7. 後端拿 prime、partner key、merchant id、訂單金額打 TapPay Pay By Prime API。
8. TapPay 驗證信用卡有效性（未過期、未凍結、額度足夠）並進行扣款。
9. TapPay 回傳結果：
    - 成功（status = 0）：新增付款紀錄，將訂單狀態改為 paid。
    - 失敗（status != 0）：訂單保持 unpaid，回傳錯誤訊息給前端。

---

## 關鍵字名詞解釋

### TapPay SDK
- 定義：由 TapPay 官方提供的 JavaScript 套件，讓前端收集使用者輸入的信用卡資訊並加密後送到 TapPay 換取 prime。
- 功能：
    - 驗證基本格式（卡號、到期日、安全碼）。
    - 將卡資料加密，送到 TapPay，取得 prime。
    - 信用卡資料不經過你的伺服器，符合 PCI-DSS 資安規範。

### access token
- 定義：使用者登入後，伺服器簽發給前端的一個身份憑證（通常是 JWT 格式），代表「我是誰」。
- 用途：
    - 鑑別身份，讓後端知道是誰在下單。
- 錯誤常見誤解：
    - access token 跟付款無關，它只是用來辨識使用者身份。

### prime
- 定義：TapPay SDK 取得的一次性交易憑證，代表剛剛輸入的信用卡資訊已通過 TapPay 初步驗證。
- 特性：
    - 有效時間短，且只能使用一次。
- 用途：
    - 後端拿 prime 向 TapPay 請求扣款。

### partner key
- 定義：TapPay 發給後端伺服器的一組密鑰，用來在打 API 時認證身份。
- 用途：
    - 放在後端呼叫 TapPay API 的 HTTP Header 中。
    - 不能暴露給前端，否則會有重大資安風險。

### merchant id
- 定義：TapPay 配發給每個商家的識別代碼，標示是哪一家商戶進行交易。
- 用途：
    - 在打 API 時，讓 TapPay知道是哪一個商家要扣款。
    - 每個商家一組，與最終金流結算帳戶相關聯。

### TapPay Pay By Prime API
- 定義：TapPay 提供的付款專用 API，後端使用 prime + 金額 + 商家資訊打這支 API 請求付款。
- 用途：
    - 驗證 prime 是否有效。
    - 驗證信用卡是否可交易（過期、凍結、額度檢查）。
    - 完成扣款。

---

## 為什麼要用 prime？

- 信用卡資料屬於高敏感資訊。
- 後端如果直接接觸卡資料，必須符合 PCI-DSS 資安標準。
- PCI-DSS 標準成本極高、技術要求極高、風險極大。
- 使用 prime，可以完全避免後端接觸信用卡資料。
- 讓資安責任交給 TapPay，系統只要處理 prime，就能安全合法並大幅降低開發與維護成本。那

/order/checkout
POST
Request Headers:
Content-Type application/json
Authorization Bearer x48aDD534da8ADSD1XC4SD5S

checkout api request body
```json
{
  "prime": "ccc1491581661f700bcc1cafec673c741f0665ca77550fe828ef38ee1437a2b8",
  "order": {
    "shipping": "delivery",
    "payment": "credit_card",
    "subtotal": 1234,
    "freight": 14,
    "total": 1300,
    "recipient": {
      "name": "Luke",
      "phone": "0987654321",
      "email": "luke@gmail.com",
      "address": "市政府站",
      "time": "morning"
    },
    "list": [
      {
        "id": "201807202157",
        "name": "活力花紋長筒牛仔褲",
        "price": 1299,
        "color": {
          "code": "DDF0FF",
          "name": "淺藍"
        },
        "size": "M",
        "qty": 1
      }
    ]
  }
}

```

table 設計(我都寫在week_0_part_3)

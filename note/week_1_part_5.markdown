
# Week 1 Part 5
Campaign api
/GET
{{https_domain}}/api/v1/marketing/campaigns
前端會打這支 api 拿到 行銷看板資訊
會一次把資料全部拿出來，可能做成輪播圖
一個行銷活動只對應到一個產品
reposne 應該會像是這樣
```json
{
  "data": [
    {
      "prodct_id": 1,
      "picture": "https://i.imgur.com/1.jpg",
      "story": "這是行銷活動的故事1"
    },
    {
      "prodct_id": 2,
      "picture": "https://i.imgur.com/2.jpg",
      "story": "這是行銷活動的故事2"
    },
    {
      "prodct_id": 3,
      "picture": "https://i.imgur.com/3.jpg",
      "story": "這是行銷活動的故事3"
    }
  ]
}
```
這是打後台得到的 api response 
我們要先做一個往db插入資料的後台系統表單
可以選擇某個商品
然後把該張品跟某個campaign結合
會再多做一個api去打product table 找出所有商品名稱
然後前端寫一個表單
可以選擇商品
並且上傳campaign圖片跟故事
存到資料庫

所以我要先寫一個直接找出全部商品的api
會找出全部商品的 id 跟名稱，以下拉選單的狀態顯示
然後在前端選擇商品後(自動選擇商品id)
並且在表單輸入 campaign的圖片跟故事
就可以直接送到後端


CREATE TABLE `Campaign` (
id bigint NOT NULL AUTO_INCREMENT,
product_id bigint NOT NULL,
picture varchar(255) DEFAULT NULL,
story TEXT DEFAULT NULL,
PRIMARY KEY (id),
FOREIGN KEY (product_id) REFERENCES Product(id)
)
//當畫面 loading 完成 html 檔案
$(document).ready(() => {
  //點擊送出表單按鈕
  $('#submit').on('click', async (event) => {
    event.preventDefault(); //防止表單刷新
    let formData = getFormData();
    try {
      let response = await fetchProductApi(formData);
      handleApiResponse(response);
    } catch (error) {
      alert('提交失敗，請檢查錯誤');
    }
  });
});

//設定打 api 參數
const API_CONFIG = {
  url: '/admin/upload',
  method: 'POST',
};

//設定抓取表單資訊的方法
function getFormData() {
  let formData = new FormData();

  formData.append('category', $('#category').val());
  formData.append('title', $('#title').val());
  formData.append('description', $('#description').val());
  formData.append('price', $('#price').val());
  formData.append('texture', $('#texture').val());
  formData.append('place', $('#place').val());
  formData.append('story', $('#story').val());
  formData.append('wash', $('#wash').val());
  formData.append('note', $('#note').val());

  let colorObj = [];
  let sizeObj = [];
  let variantObj = [];

  $('.variant-group').each(function () {
    //每一次loop到的變體
    let colorCodeData = $(this).find('.colorCode').val();
    let colorNameData = $(this).find('.colorName').val();
    let sizeData = $(this).find('.size').val();
    let stockData = $(this).find('.stock').val();

    if (colorCodeData && colorNameData) {
      colorObj.push({
        code: colorCodeData,
        name: colorNameData,
      });
    }
    if (sizeData) {
      sizeObj.push({
        size: sizeData,
      });
    }
    if (colorCodeData && colorNameData && sizeData && stockData) {
      variantObj.push({
        colorCode: colorCodeData,
        colorName: colorNameData,
        size: sizeData,
        stock: stockData ? parseInt(stockData) : 0,
      });
    }
  });

  let mainImage = $('#mainImage')[0].files[0];
  let images = $('#images')[0].files;

  if (mainImage) {
    formData.append('mainImage', mainImage);
  }
  if (images.length > 0) {
    for (let image of images) {
      formData.append('image[]', image);
    }
  };

  formData.append('color', JSON.stringify(colorObj));
  formData.append('size', JSON.stringify(sizeObj));
  formData.append('variant', JSON.stringify(variantObj));

  return formData;
};

//按下新增遍體
$('ul').on('click', '.addVariant', function () {
  let newVariant =
    `<li>` +
    `<div class="variant-group">` +
    `<input type="text" class="colorCode" placeholder="顏色代碼">` +
    `<input type="text" class="colorName" placeholder="顏色名稱">` +
    `<input type="text" class="size" placeholder="尺寸">` +
    `<input type="text" class="stock" placeholder="庫存">` +
    `<button type="button" class="removeVariant">移除</button>` +
    `<button type="button" class="addVariant">新增</button>` +
    `</div>` +
    `</li>`;
  $(this).closest('li').after(newVariant);
});

//移除變體
$('ul').on('click', '.removeVariant', function () {
  let currentLi = $(this).closest('li');
  //如果為第一個變體，則無法移除
  if (currentLi[0] === $('ul li')[0]) {
    return;
  }
  currentLi.remove();
});

//通用的 api 函數
async function fetchApi(url, method, data) {
  return fetch(url, {
    method: method,
    body: data,
  })
    .then((response) => {
      console.log('response; ', response);

      if (response.status != 201) {
        throw new Error(`商品表單API異常 ${response.statusText}`);
      }
      return response.json();
    })
    .catch((error) => {
      console.log(`發生錯誤`, error);
      throw error;
    });
};

function fetchProductApi(formData) {
  return fetchApi(API_CONFIG.url, API_CONFIG.method, formData);
};

function handleApiResponse(response) {
  console.log('API response ', response);
  alert('提交成功');
};

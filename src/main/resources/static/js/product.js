/**
 * 當 DOM 下載完成，啟動以下程式碼，為此程式碼的啟動點
 * @async
 * @author tashuchiu
 */
$(document).ready(() => {
  //點擊送出表單按鈕
  $('#submit').on('click', async (event) => {
    event.preventDefault(); //防止表單刷新
    try {
      const formData = getFormData(); //拿到表單資料物件
      const apiParam = getApiParam('productFrom', formData); //拿到打api的參數物件

      //檢查非空值
      const entireApiParam = apiParam?.data?.entries();
      const apiParamArray = [];
      for (const entry of entireApiParam) {
        apiParamArray.push(entry);
      }
      if (apiParamArray.length === 0) {
        alert('請求參數或表單不能為空');
        return;
      }

      fetchProductApi(apiParam) //送出表單 打 post api
        .then((response) => {
          console.log('response: ', response);
          alert('成功上傳表單');
        })
        .catch((error) => {
          console.log('error: ', error);
          alert('上傳檔案失敗');
        });
    } catch (error) {
      alert('提交失敗，請檢查錯誤');
    }
  });
});

//------------------ 個需求 api 的邏輯 ------------------

const fetchProductApi = (apiParam) => {
  return $.ajax({
    url: apiParam?.url,
    type: apiParam?.method,
    contentType: false,
    processData: false,
    data: apiParam?.data,
  });
};

//------------------ 抓取表單邏輯 ------------------

/**
 * 傳入參數，組成打 api 的請求，可以擴充
 * @param {String} type // API 種類
 * @param {Object} data // 發出的資料
 * @returns
 */
const getApiParam = (type, data) => {
console.log("data: ",JSON.stringify(data));
  //表單提交
  if (type == 'productFrom') {
    return {
      url: '/admin/upload',
      method: 'POST',
      data: data || {},
    };
  }
  return {};
};

//------------------ 抓取表單邏輯 ------------------

/**
 * 抓取表單資訊的方法
 * @return {Object}
 * @author tashuchiu
 */
function getFormData() {
  const formData = new FormData();

  formData.append('category', $('#category').val());
  formData.append('title', $('#title').val());
  formData.append('description', $('#description').val());
  formData.append('price', $('#price').val());
  formData.append('texture', $('#texture').val());
  formData.append('place', $('#place').val());
  formData.append('story', $('#story').val());
  formData.append('wash', $('#wash').val());
  formData.append('note', $('#note').val());

  const colorObj = [];
  const sizeObj = [];
  const variantObj = [];
  const variantSet = new Set(); //確認變體是否重複

  $('.variant-group').each(function () {
    //每一次loop到的變體
    const colorCodeData = $(this).find('.colorCode').val();
    const colorNameData = $(this).find('.colorName').val();
    const sizeData = $(this).find('.size').val();
    const stockData = $(this).find('.stock').val();
    const key = `${colorCodeData}-${sizeData}`;
    let hasDuplicateVariantKey = false;
    if (variantSet.has(key)) {
      alert(`發現重複變體: 顏色: ${colorCodeData}/尺寸 : ${sizeData}`);
      hasDuplicateVariantKey = true;
      return false;
    }

    variantSet.add(key);

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
    if (hasDuplicateVariantKey == false) {
      return {};
    }
  });

  const mainImage = $('#mainImage')[0].files[0];
  const images = $('#images')[0].files;

  if (mainImage) {
    formData.append('mainImage', mainImage);
  }
  if (images.length > 0) {
    for (let image of images) {
      formData.append('images[]', image);
    }
  }

  formData.append('variant', JSON.stringify(variantObj));

  for (const value of formData.values()) {
    console.log('value: ' + value);
  }
  return formData;
}

//------------------  新增 刪除 變體 ------------------
/**
 * 按下新增variant
 * @author tashuchiu
 */
$('ul').on('click', '.addVariant', function () {
  const newVariant =
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

/**
 * 按下移除variant
 * @author tashuchiu
 */
$('ul').on('click', '.removeVariant', function () {
  const currentLi = $(this).closest('li');
  //如果為第一個變體，則無法移除
  if (currentLi[0] === $('ul li')[0]) {
    return;
  }
  currentLi.remove();
});

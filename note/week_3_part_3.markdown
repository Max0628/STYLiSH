
k6 load test javascript
```js
import http from 'k6/http';

export const options = {
  vus: 2, // inmplementation 2 vitural user (VUs)
  iterations: 40, // every VU will make 20 requests
  insecureSkipTLSVerify: true,
};

const ipList = ['111.111.111.111', '222.222.222.222'];

export default function () {
  const ip = ipList[__VU - 1]; //every VU will use a different IP address from the list
  const headers = {
    'X-Forwarded-For': ip,
  };

  const res = http.get('https://127.0.0.1:8443/api/v1/products/women?paging=2', { headers });
  console.log(`VU: ${__VU}, IP: ${ip}, Response code: ${res.status}`);
}
```
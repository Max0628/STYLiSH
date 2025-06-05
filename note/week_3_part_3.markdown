
k6 load test javascript
```js
import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    ip1: {
      executor: 'constant-arrival-rate',
      rate: 20, // 每秒 20 次
      duration: '1s',
      timeUnit: '1s',
      preAllocatedVUs: 20,
      exec: 'user1',
    },
    ip2: {
      executor: 'constant-arrival-rate',
      rate: 20,
      duration: '1s',
      timeUnit: '1s',
      preAllocatedVUs: 20,
      exec: 'user2',
    },
  },
  insecureSkipTLSVerify: true,
};

export function user1() {
  let headers = { 'X-Forwarded-For': '111.111.111.111' };
  let res = http.get('https://127.0.0.1:8443/api/v1/products/women?paging=2', { headers: headers });
  check(res, {
    '狀態碼是 200 或 429': (r) => r.status === 200 || r.status === 429,
  });
  console.log('IP: 111.111.111.111 | 狀態: ' + res.status);
}

export function user2() {
  let headers = { 'X-Forwarded-For': '222.222.222.222' };
  let res = http.get('https://127.0.0.1:8443/api/v1/products/women?paging=2', { headers: headers });
  check(res, {
    '狀態碼是 200 或 429': (r) => r.status === 200 || r.status === 429,
  });
  console.log('IP: 222.222.222.222 | 狀態: ' + res.status);
}
```
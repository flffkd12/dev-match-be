import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';

export const BASE_URL = 'http://host.docker.internal:8080';

// 테스트 공통 설정
export const CONFIG = {
  vus: 100,
  perVuIterations: 8,      // create
  sharedIterations: 800,   // read
  oneIteration: 1,          // self-read, update, delete
};

let cachedToken = null;

function loginAndGetToken() {
  const setupRes = http.post(
      `${BASE_URL}/test/users/login?oauthId=${exec.vu.idInTest}`, null, {
        tags: {name: 'POST /test/users/login'}
      });

  const success = check(setupRes, {'사용자 로그인 성공': (r) => r.status === 200});
  return success ? setupRes.json().content : null;
}

function setAuthCookie(token) {
  const jar = http.cookieJar();
  jar.set(BASE_URL, 'accessToken', token, {
    domain: 'host.docker.internal',
    path: '/',
    secure: false,
    httpOnly: true,
    sameSite: 'strict',
  });
}

export function setAuth() {
  if (!cachedToken) {
    cachedToken = loginAndGetToken();
  }
  if (cachedToken) {
    setAuthCookie(cachedToken);
  }
}
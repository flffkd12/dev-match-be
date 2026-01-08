import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';

export const options = {
  scenarios: {
    delete_data: {
      executor: 'per-vu-iterations',
      vus: 100,
      iterations: 30,
      exec: 'deleteProject',
    },
  },
};

const BASE_URL = 'http://host.docker.internal:8080';
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

export function deleteProject() {
  if (!cachedToken) {
    cachedToken = loginAndGetToken();
  }
  setAuthCookie(cachedToken);

  const params = {tags: {name: 'DELETE /projects/{projectId}'}};
  const response = http.del(
      `${BASE_URL}/projects/${exec.scenario.iterationInInstance + 1}`, null,
      params);
  check(response, {'프로젝트 삭제 성공 (204)': (r) => r.status === 204});
}

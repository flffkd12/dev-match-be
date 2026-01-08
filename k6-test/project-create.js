import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';

export const options = {
  scenarios: {
    setup_data: {
      executor: 'per-vu-iterations',
      vus: 100,
      iterations: 30,
      exec: 'createProject',
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

export function createProject() {
  if (!cachedToken) {
    cachedToken = loginAndGetToken();
  }
  setAuthCookie(cachedToken);

  const payload = JSON.stringify({
    title: `Project-VU${exec.vu.idInTest}-IT${exec.scenario.iterationInInstance
    + 1}`,
    description: '부하 테스트를 위해 자동으로 생성된 프로젝트 설명입니다.',
    techStacks: ['Java', 'Spring Boot', 'React'],
    teamSize: 4,
    durationWeeks: 8
  });

  const params = {
    headers: {'Content-Type': 'application/json'},
    tags: {name: 'POST /projects'}
  };

  const response = http.post(`${BASE_URL}/projects`, payload, params);
  check(response, {'프로젝트 생성 성공 (201)': (r) => r.status === 201});
}

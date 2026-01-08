import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';

export const options = {
  scenarios: {
    update_data: {
      executor: 'shared-iterations',
      vus: 100,
      iterations: 3000,
      exec: 'updateProject',
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

export function updateProject() {
  if (!cachedToken) {
    cachedToken = loginAndGetToken();
  }
  setAuthCookie(cachedToken);

  const payload = JSON.stringify({
    title: `Project-VU${exec.vu.idInTest}-IT${exec.scenario.iterationInInstance}-Updated`,
    description: '부하 테스트 중 수정된 프로젝트 설명입니다.',
    techStacks: ['Java', 'Spring Boot', 'Vue.js'],
    teamSize: 5,
    durationWeeks: 10,
    roleAssignment: 'Backend: 2, Frontend: 2, Designer: 1'
  });

  const params = {
    headers: {'Content-Type': 'application/json'},
    tags: {name: 'PATCH /projects/{projectId}'}
  };
  const response = http.patch(
      `${BASE_URL}/projects/${exec.scenario.iterationInInstance + 1}`, payload,
      params);
  check(response, {'프로젝트 수정 성공 (200)': (r) => r.status === 200});
}

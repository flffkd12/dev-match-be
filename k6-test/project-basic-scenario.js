import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';

// 가상의 사용자 x명이 각각 y개의 프로젝트 생성하여 총 xy개 프로젝트 생성
export const options = {
  scenarios: {
    my_scenario: {
      executor: 'per-vu-iterations',
      vus: 100,
      iterations: 8,
    },
  },
};

const BASE_URL = 'http://host.docker.internal:8080';
let cachedToken = null;

export default function () {
  if (cachedToken == null) {
    cachedToken = loginAndGetToken();
  }

  if (cachedToken != null) {
    setAuthCookie(cachedToken);
    const response = createProject();
    check(response, {'프로젝트 생성 성공 (201)': (r) => r.status === 201});
  }
}

function loginAndGetToken() {
  const setupRes = http.post(`${BASE_URL}/test/users/login`, null, {
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

function createProject() {
  const payload = JSON.stringify({
    title: `Project-VU${exec.vu.idInTest}-IT${exec.scenario.iterationInInstance}`,
    description: '부하 테스트를 위해 자동으로 생성된 프로젝트 설명입니다.',
    techStacks: ['Java', 'Spring Boot', 'React'],
    teamSize: 4,
    durationWeeks: 8
  });

  const params = {
    headers: {'Content-Type': 'application/json'},
    tags: {name: 'POST /projects'}
  };

  return http.post(`${BASE_URL}/projects`, payload, params);
}

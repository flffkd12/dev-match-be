import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';

/**
 * 1. 가상의 사용자 x명이 각각 y개의 프로젝트를 생성한다.
 * 2. 가상의 사용자 x명이 각각 y개의 프로젝트를 조회해 본다.
 * 3. 가상의 사용자 x명이 각각 y개의 프로젝트를 수정한다.
 */
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
  if (!cachedToken) {
    cachedToken = loginAndGetToken();
  }

  if (cachedToken) {
    setAuthCookie(cachedToken);
    const response = createProject();
    const creationSuccess = check(response,
        {'프로젝트 생성 성공 (201)': (r) => r.status === 201});

    if (creationSuccess) {
      const projectId = response.json().content.projectId;
      const getResponse = getProject(projectId);
      const getSuccess = check(getResponse,
          {'프로젝트 단일 조회 성공 (200)': (r) => r.status === 200});

      if (getSuccess) {
        const updateResponse = updateProject(projectId);
        check(updateResponse, {'프로젝트 수정 성공 (200)': (r) => r.status === 200});
      }
    }
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

function getProject(projectId) {
  const params = {
    tags: {name: 'GET /projects/{projectId}'}
  };

  return http.get(`${BASE_URL}/projects/${projectId}`, params);
}

function updateProject(projectId) {
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

  return http.patch(`${BASE_URL}/projects/${projectId}`, payload, params);
}

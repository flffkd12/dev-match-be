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
  // 1. VU가 처음 실행될 때만 사용자 생성
  if (!cachedToken) {
    const setupRes = http.post(`${BASE_URL}/test/users/login`, null, {
      tags: {name: 'POST /test/users/login'}
    });

    if (check(setupRes, {'사용자 생성 성공': (r) => r.status === 200})) {
      cachedToken = setupRes.json().content;
    }
  }

  // 2. 쿠키 설정
  const jar = http.cookieJar();
  jar.set(BASE_URL, 'accessToken', cachedToken, {
    domain: 'host.docker.internal',
    path: '/',
    secure: false,
    httpOnly: true,
    sameSite: 'strict',
  });

  // 3. 프로젝트 생성 요청 데이터 준비
  const payload = JSON.stringify({
    title: `Project-VU${exec.vu.idInTest}-IT${exec.scenario.iterationInInstance}`,
    description: '부하 테스트를 위해 자동으로 생성된 프로젝트 설명입니다.',
    techStacks: ['Java', 'Spring Boot', 'React'],
    teamSize: 4,
    durationWeeks: 8
  });

  const params = {
    headers: {'Content-Type': 'application/json',},
    tags: {name: 'POST /projects'}
  };

  // 4. 프로젝트 생성
  const projectRes = http.post(`${BASE_URL}/projects`, payload, params);
  check(projectRes, {'프로젝트 생성 성공 (201)': (r) => r.status === 201,});
}

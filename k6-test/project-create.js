import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';
import {BASE_URL, CONFIG, setAuth} from './utils.js';

export const options = {
  scenarios: {
    setup_data: {
      executor: 'per-vu-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.perVuIterations,
      exec: 'createProject',
    },
  },
};

export function createProject() {
  setAuth();

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

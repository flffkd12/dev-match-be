import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';
import {BASE_URL, CONFIG, setAuth} from './utils.js';

export const options = {
  scenarios: {
    update_data: {
      executor: 'shared-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.sharedIterations,
      exec: 'updateProject',
    },
  },
};

export function updateProject() {
  setAuth();

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

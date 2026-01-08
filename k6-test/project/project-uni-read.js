import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';
import {BASE_URL, CONFIG, setAuth} from '../utils.js';

export const options = {
  scenarios: {
    fetch_uni_data: {
      executor: 'shared-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.sharedIterations,
      exec: 'getProject',
    },
  },
};

export function getProject() {
  setAuth();

  const params = {tags: {name: 'GET /projects/{projectId}'}};
  const response = http.get(
      `${BASE_URL}/projects/${exec.scenario.iterationInInstance + 1}`, params);
  check(response, {'프로젝트 단일 조회 성공 (200)': (r) => r.status === 200});
}

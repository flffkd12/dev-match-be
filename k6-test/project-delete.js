import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';
import {BASE_URL, CONFIG, setAuth} from './utils.js';

export const options = {
  scenarios: {
    delete_data: {
      executor: 'per-vu-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.perVuIterations,
      exec: 'deleteProject',
    },
  },
};

export function deleteProject() {
  setAuth();

  const params = {tags: {name: 'DELETE /projects/{projectId}'}};
  const response = http.del(
      `${BASE_URL}/projects/${exec.scenario.iterationInInstance + 1}`, null,
      params);
  check(response, {'프로젝트 삭제 성공 (204)': (r) => r.status === 204});
}

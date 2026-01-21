import http from 'k6/http';
import {check} from 'k6';
import {BASE_URL, CONFIG, setAuth} from '../common/utils.js';

export const options = {
  scenarios: {
    fetch_all_projects: {
      executor: 'constant-vus',
      vus: CONFIG.vus,
      duration: '30s',
      exec: 'getAllProjects',
    },
  },
};

export function getAllProjects() {
  setAuth();

  const params = {tags: {name: 'GET /projects'}};
  const response = http.get(
      `${BASE_URL}/projects?size=${CONFIG.sharedIterations}`, params);
  check(response, {'프로젝트 전체 조회 성공 (200)': (r) => r.status === 200});
}

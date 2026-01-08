import http from 'k6/http';
import {check} from 'k6';
import {BASE_URL, CONFIG, setAuth} from './utils.js';

export const options = {
  scenarios: {
    fetch_my_projects: {
      executor: 'per-vu-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.oneIteration,
      exec: 'getMyProjects',
    },
  },
};

export function getMyProjects(doAuth = true) {
  if (doAuth) {
    setAuth();
  }

  const params = {tags: {name: 'GET /projects/my'}};
  const response = http.get(`${BASE_URL}/projects/my`, params);

  const isSuccess = check(response, {
    '본인 프로젝트 목록 조회 성공 (200)': (r) => r.status === 200,
    '응답 데이터 존재 확인': (r) => r.json().content !== undefined,
  });

  if (isSuccess) {
    return response.json().content.content;
  }
  return [];
}

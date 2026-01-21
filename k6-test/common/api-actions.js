import http from 'k6/http';
import {check} from 'k6';
import {BASE_URL} from './utils.js';

export function createProject(payload) {
  const params = {
    headers: {'Content-Type': 'application/json'},
    tags: {name: 'POST /projects'}
  };

  const res = http.post(`${BASE_URL}/projects`, JSON.stringify(payload),
      params);

  check(res, {'프로젝트 생성 성공 (201)': (r) => r.status === 201});
  return res;
}

export function createApplication(payload) {
  const params = {
    headers: {'Content-Type': 'application/json'},
    tags: {name: 'POST /applications'}
  };

  const res = http.post(`${BASE_URL}/applications`, JSON.stringify(payload),
      params);

  check(res, {'지원서 제출 성공 (201)': (r) => r.status === 201});
  return res;
}

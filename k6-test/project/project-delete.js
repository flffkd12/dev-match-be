import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';
import {BASE_URL, CONFIG, setAuth} from '../common/utils.js';
import {getMyProjects} from './project-self-read.js';

export const options = {
  scenarios: {
    delete_my_projects: {
      executor: 'per-vu-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.oneIteration,
      exec: 'deleteAllMyProjects',
    },
  },
};

export function deleteAllMyProjects() {
  setAuth();

  const projects = getMyProjects(false);

  if (!projects || projects.length === 0) {
    console.log(`VU ${exec.vu.idInTest}: 삭제할 프로젝트가 없습니다.`);
    return;
  }

  projects.forEach((project) => {
    const params = {tags: {name: 'DELETE /projects/{projectId}'}};
    const deleteRes = http.del(`${BASE_URL}/projects/${project.projectId}`,
        null, params);

    check(deleteRes, {
      '프로젝트 삭제 성공 (204)': (r) => r.status === 204,
    });
  });
}

import http from 'k6/http';
import {check} from 'k6';
import exec from 'k6/execution';
import {BASE_URL, CONFIG, setAuth} from '../common/utils.js';
import {getMyProjects} from './project-self-read.js';

export const options = {
  scenarios: {
    update_my_projects: {
      executor: 'per-vu-iterations',
      vus: CONFIG.vus,
      iterations: CONFIG.oneIteration,
      exec: 'updateAllMyProjects',
    },
  },
};

export function updateAllMyProjects() {
  setAuth();

  const projects = getMyProjects(false);

  if (!projects || projects.length === 0) {
    console.log(`VU ${exec.vu.idInTest}: 수정할 프로젝트가 없습니다.`);
    return;
  }

  projects.forEach((project) => {
    const payload = JSON.stringify({
      title: `Project-VU${exec.vu.idInTest}-ID${project.id}-Updated`,
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

    const updateRes = http.patch(`${BASE_URL}/projects/${project.projectId}`,
        payload, params);

    check(updateRes, {
      '프로젝트 수정 성공 (200)': (r) => r.status === 200,
    });
  });
}

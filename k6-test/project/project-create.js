import exec from 'k6/execution';
import {CONFIG, setAuth} from '../utils.js';
import * as api from '../common/api-actions.js';

export const options = {
  scenarios: {
    setup_data: {
      executor: 'shared-iterations',
      vus: 8,
      iterations: CONFIG.sharedIterations,
      exec: 'createProject',
    },
  },
};

export function createProject() {
  setAuth();

  const payload = {
    title: `Project-VU${exec.vu.idInTest}-IT${exec.scenario.iterationInInstance
    + 1}`,
    description: '부하 테스트를 위해 자동으로 생성된 프로젝트 설명입니다.',
    techStacks: ['Java', 'Spring Boot', 'React'],
    teamSize: 4,
    durationWeeks: 8
  };

  api.createProject(payload);
}

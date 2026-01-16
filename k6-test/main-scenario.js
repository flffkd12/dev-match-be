import {setAuth} from './utils.js';
import * as api from './common/api-actions.js';

// 프로젝트 정원 설정
const TEAM_SIZE = 5;

export const options = {
  scenarios: {
    apply_to_project: {
      executor: 'shared-iterations',
      vus: TEAM_SIZE,
      iterations: TEAM_SIZE,
      exec: 'applyScenario',
    },
  },
};

/** 전역 설정: 프로젝트를 하나 생성하고 ID 공유 */
export function setup() {
  setAuth();

  const payload = {
    title: "메인 시나리오 프로젝트",
    description: "정원을 모두 채우는 시나리오 테스트용 프로젝트입니다.",
    techStacks: ['Java', 'Spring Boot', 'React'],
    teamSize: TEAM_SIZE,
    durationWeeks: 12
  };

  const res = api.createProject(payload);
  const projectId = res.json().content.projectId;

  console.log(`[Setup] 프로젝트 생성 완료. ID: ${projectId}, 정원: ${TEAM_SIZE}`);

  return {projectId: projectId};
}

/** 지원서 제출 시나리오 */
export function applyScenario(data) {
  setAuth();

  const payload = {
    projectId: data.projectId,
    skills: [
      {techStack: 'Java', techScore: Math.floor(Math.random() * 5) + 5},
      {techStack: 'React', techScore: Math.floor(Math.random() * 5) + 5}
    ]
  };

  api.createApplication(payload);
}

import http from 'k6/http';
import {check} from 'k6';

/** 부하 테스트를 위한 사용자 데이터 셋업*/
export const options = {
  iterations: 100,
  vus: 100,
};

export default function () {
  const url = 'http://host.docker.internal:8080/test/users/setup';

  const res = http.post(url);

  check(res, {
    '유저 생성 성공 (200)': (r) => r.status === 200,
  });
}

package com.devmatch.backend.domain.application.dto.request;

import com.devmatch.backend.domain.application.dto.Skill;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ApplicationCreateRequest(

    @NotNull(message = "프로젝트 ID는 필수입니다.")
    @Positive(message = "올바른 프로젝트 ID 형식이 아닙니다.")
    Long projectId,

    @NotEmpty(message = "최소 하나 이상의 기술 스택을 입력해야 합니다.")
    @Size(max = 30, message = "기술 스택은 최대 30개까지 입력할 수 있습니다.")
    List<@Valid Skill> skills
) {

}
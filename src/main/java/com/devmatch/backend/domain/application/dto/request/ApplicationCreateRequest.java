package com.devmatch.backend.domain.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ApplicationCreateRequest(
    @NotNull(message = "프로젝트 ID는 필수입니다.")
    @Positive(message = "올바른 프로젝트 ID 형식이 아닙니다.")
    Long projectId,

    @NotEmpty(message = "기술 스택은 하나 이상 있어야합니다.")
    @Size(max = 30, message = "기술 스택은 최대 30개까지 입력할 수 있습니다.")
    List<@NotBlank String> techStacks,

    @NotEmpty(message = "기술 점수를 기재해 주세요.")
    @Size(max = 30, message = "기술 점수는 최대 30개까지 입력할 수 있습니다.")
    List<@Min(1) @Max(10) Integer> techScores
) {

}
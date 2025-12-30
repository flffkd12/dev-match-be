package com.devmatch.backend.domain.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Skill(

    @NotBlank(message = "기술 스택 이름은 필수입니다.")
    String techStack,

    @NotNull(message = "기술 점수는 필수입니다.")
    @Min(value = 1, message = "점수는 최소 1점 이상이어야 합니다.")
    @Max(value = 10, message = "점수는 최대 10점 이하여야 합니다.")
    Integer techScore
) {

}

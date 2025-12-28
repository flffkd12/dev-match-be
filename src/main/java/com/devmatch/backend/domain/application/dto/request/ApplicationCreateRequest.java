package com.devmatch.backend.domain.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ApplicationCreateRequest(

    @NotNull(message = "프로젝트 ID는 필수입니다.")
    @Positive(message = "올바른 프로젝트 ID 형식이 아닙니다.")
    Long projectId,

    @Size(max = 30, message = "기술 스택은 최대 30개까지 입력할 수 있습니다.")
    List<@Valid SkillRequest> skills
) {

  public record SkillRequest(

      @NotBlank(message = "기술 스택 이름은 필수입니다.")
      String techStack,

      @NotNull(message = "기술 점수는 필수입니다.")
      @Min(value = 1, message = "점수는 최소 1점 이상이어야 합니다.")
      @Max(value = 10, message = "점수는 최대 10점 이하여야 합니다.")
      Integer techScore
  ) {

  }
}
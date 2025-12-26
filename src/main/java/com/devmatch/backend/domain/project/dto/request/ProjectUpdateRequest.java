package com.devmatch.backend.domain.project.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProjectUpdateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    String title,

    @NotBlank(message = "설명은 필수입니다.")
    @Size(max = 2000, message = "설명은 2000자 이하여야 합니다.")
    String description,

    @NotEmpty(message = "기술 스택은 하나 이상 있어야합니다.")
    @Size(max = 30, message = "기술 스택은 최대 30개까지입니다.")
    List<String> techStacks,

    @NotNull(message = "팀 규모를 입력해주세요.")
    @Min(value = 1, message = "팀 규모는 1명 이상이어야 합니다.")
    Integer teamSize,

    @NotNull(message = "기간을 입력해주세요.")
    @Min(value = 1, message = "기간은 1주 이상이어야 합니다.")
    Integer durationWeeks,

    @NotNull(message = "역할 분담 내용을 입력해주세요.")
    @Size(max = 2000, message = "역할 분담 내용은 2000자 이하여야 합니다.")
    String roleAssignment
) {

}

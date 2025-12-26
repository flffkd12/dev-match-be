package com.devmatch.backend.domain.application.dto.request;

import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusUpdateRequest(
    @NotNull(message = "지원서 상태를 넣어주세요.")
    ApplicationStatus status
) {

}
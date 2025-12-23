package com.devmatch.backend.domain.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProjectApplyRequest(
    @NotNull Long projectId,
    @NotNull List<String> techStacks,
    @NotNull List<Integer> techScores
) {

}
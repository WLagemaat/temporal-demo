package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;

@Builder
public record TransgressionProcessingResult(
        String transgressionNumber,
        boolean succeeded,
        boolean isManualTask,
        String errorMessage
){
}

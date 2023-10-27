package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;

@Builder
public record FineProcessingResult(
        String transgressionNumber,
        boolean succeeded,
        boolean isManualTask,
        String value,
        String errorMessage
){
}

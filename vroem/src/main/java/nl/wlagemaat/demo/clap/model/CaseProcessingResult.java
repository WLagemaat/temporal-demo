package nl.wlagemaat.demo.clap.model;

import lombok.Builder;

@Builder
public record CaseProcessingResult(
        String insuranceCaseNumber,
        boolean succeeded,
        boolean isManualTask,
        String value,
        String errorMessage
){
}

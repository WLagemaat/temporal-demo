package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;

@Builder
public record TaskProcessingResult(
        String transgressionNumber,
        boolean succeeded,
        String errorMessage
){
}

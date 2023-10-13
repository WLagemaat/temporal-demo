package nl.wlagemaat.demo.bas.model;

import lombok.Builder;

@Builder
public record TaskProcessingResult(
        String transgressionNumber,
        boolean succeeded,
        String errorMessage
){
}

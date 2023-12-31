package nl.wlagemaat.demo.clients.model;

import lombok.Builder;

@Builder
public record TaskProcessingResult(
        String insuranceCaseNumber,
        String value,
        boolean succeeded,
        String errorMessage
){
}

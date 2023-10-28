package nl.wlagemaat.demo.clients.model;

import lombok.Builder;

@Builder
public record InsuranceCaseValidationEnrichmentResult(
        String insuranceCaseNumber,
        boolean isValid,
        boolean isMinorSeverity,
        String errorMessage
){
}

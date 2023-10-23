package nl.wlagemaat.demo.clients.model;

import lombok.Builder;

@Builder
public record TransgressionValidationEnrichmentResult(
        String transgressionNumber,
        boolean isValid,
        boolean isMulder,
        String errorMessage
){
}

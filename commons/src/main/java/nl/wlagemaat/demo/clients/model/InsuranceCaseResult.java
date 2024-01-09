package nl.wlagemaat.demo.clients.model;

import lombok.Builder;

@Builder
public record InsuranceCaseResult(
        String insuranceCaseNumber,
        boolean isProcessed,
        Department department,
        String errorMessage
){
}

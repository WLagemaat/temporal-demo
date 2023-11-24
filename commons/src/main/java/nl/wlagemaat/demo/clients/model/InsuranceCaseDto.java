package nl.wlagemaat.demo.clients.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record InsuranceCaseDto(
        String insuranceCaseId,
        String insuranceNumber,
        Integer validOdds,
        Integer rdwOdds,
        Integer rdwTechnicalErrorOdds,
        Boolean isMinorSeverity,
        Integer dogTechnicalErrorOdds,
        Integer catTechnicalErrorOdds,
        Integer instantPayoutTechnicalErrorOdds,
        Integer auditorTechnicalErrorOdds

) implements Serializable {

}

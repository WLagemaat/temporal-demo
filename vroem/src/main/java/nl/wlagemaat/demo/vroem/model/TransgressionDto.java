package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;

@Builder
public record TransgressionDto(
        String transgressionNumber,
        Integer validOdds,
        Integer rdwOdds,
        Integer rdwTechnicalErrorOdds,
        Boolean isMulder,
        Integer mulderTechnicalErrorOdds,
        Integer wormTechnicalErrorOdds,
        Integer svenTechnicalErrorOdds,
        Integer cvomTechnicalErrorOdds,
        Party party
) implements MQResponse {

}

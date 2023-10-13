package nl.wlagemaat.demo.bas.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record FineDto(
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
) implements Serializable {

}

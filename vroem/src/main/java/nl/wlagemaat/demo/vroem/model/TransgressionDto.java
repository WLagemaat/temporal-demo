package nl.wlagemaat.demo.vroem.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Value
public record TransgressionDto(
        String transgressionNumber,
        Integer validOdds,
        Integer rdwOdds,
        Integer rdwTechnicalErrorOdds,
        Boolean isMulder,
        Integer mulderTechnicalErrorOdds,
        Integer wormTechnicalErrorOdds,
        Integer svenTechnicalErrorOdds,
        Party party
) implements MQResponse, Serializable {

}

package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;
import nl.wlagemaat.demo.clients.model.FineDto;

import java.io.Serializable;

@Builder
public record ValidatedFineDto(
        String transgressionNumber,
        FineDto fineInput
) implements Serializable {

}

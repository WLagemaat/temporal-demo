package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ValidatedFineDto(
        String transgressionNumber,
        FineDto fineInput
) implements Serializable {

}

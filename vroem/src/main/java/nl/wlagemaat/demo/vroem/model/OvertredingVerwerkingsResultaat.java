package nl.wlagemaat.demo.vroem.model;

import lombok.Builder;

@Builder
public record OvertredingVerwerkingsResultaat(
        String overtredingsNummer,
        boolean geslaagd,
        String errorMessage
){
}

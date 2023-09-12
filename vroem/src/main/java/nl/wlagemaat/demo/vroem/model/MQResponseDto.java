package nl.wlagemaat.demo.vroem.model;

import java.io.Serializable;

public record MQResponseDto (
        String transgressionNumber,
        String message,
        Party party
) implements Serializable {}

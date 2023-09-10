package nl.wlagemaat.demo.vroem.model;

public record MQResponseDto (
        String transgressionNumber,
        String message,
        Party party
) implements MQResponse{}

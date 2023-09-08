package nl.wlagemaat.demo.vroem.model;


import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = false)
@Value
public record MQResponseDto (
        String transgressionNumber,
        String message,
        Party party
) implements MQResponse{
}

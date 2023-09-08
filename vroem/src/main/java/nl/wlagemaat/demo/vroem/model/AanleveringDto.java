package nl.wlagemaat.demo.vroem.model;

public record AanleveringDto(
        String overtredingsnummer,
        Integer validatieKans,
        Integer rdwKans,
        Integer rdwTechnischKans,
        Boolean isMulder,
        Integer wormTechnischKans,
        Integer svenTechnischKans
){}

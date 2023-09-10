package nl.wlagemaat.demo.vroem.repository.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Transgression {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String transgressionNumber;
    String licensePlate;
    boolean isMulder;
    boolean isProsecuted;
    String personConcerned;


}

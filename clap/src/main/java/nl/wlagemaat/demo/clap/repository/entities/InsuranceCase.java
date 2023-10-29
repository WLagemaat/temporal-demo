package nl.wlagemaat.demo.clap.repository.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class InsuranceCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String insuranceCaseNumber;
    String licensePlate;
    Integer insuranceTier;
    boolean isMinorSeverity;
    boolean isAudited;
    String driver;
}

package nl.wlagemaat.demo.vroem.repository.entiteiten;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Overtreding {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String overtredingsnummer;
    String kenteken;
    boolean isMulder;
    String betrokkene;


}

package nl.wlagemaat.demo.clap.repository;

import nl.wlagemaat.demo.clap.repository.entities.InsuranceCase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsuranceCaseRepository extends CrudRepository<InsuranceCase,Long> {

    Optional<InsuranceCase> findByInsuranceCaseNumber(@Param("insuranceCaseNumber") String insuranceCaseNumber);
}

package nl.wlagemaat.demo.vroem.repository;

import nl.wlagemaat.demo.vroem.repository.entiteiten.Overtreding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "overtreding", path = "overtreding")
public interface OvertredingRepository extends PagingAndSortingRepository<Overtreding, Long>, CrudRepository<Overtreding,Long> {

    Overtreding findByOvertredingsnummer(@Param("overtredingsnummer") String overtredingsnummer);
}

package nl.wlagemaat.demo.vroem.repository;

import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "transgression", path = "transgression")
public interface TransgressionRepository extends PagingAndSortingRepository<Transgression, Long>, CrudRepository<Transgression,Long> {

    Transgression findByTransgressionNumber(@Param("transgressionNumber") String transgressionNumber);
}

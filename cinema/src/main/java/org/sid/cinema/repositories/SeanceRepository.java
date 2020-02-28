package org.sid.cinema.repositories;

// import java.util.Date;
// import java.util.List;

import org.sid.cinema.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SeanceRepository extends JpaRepository<Seance, Long> {

    // List<Seance> findByHeureDebut(Date heureDebut);

}
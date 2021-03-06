package org.lpro.backoffice.boundary;

import org.lpro.backoffice.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;

public interface PartieResource extends CrudRepository<Partie, String> {

    Page<Partie> findAll(Pageable pegeable);

    List<Partie> findAll();

    Optional<Partie> findById(String id);

}
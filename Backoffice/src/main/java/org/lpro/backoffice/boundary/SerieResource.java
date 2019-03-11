package org.lpro.backoffice.boundary;

import org.lpro.backoffice.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;

public interface SerieResource extends CrudRepository<Serie, String> {

    Page<Serie> findAll(Pageable pegeable);

    List<Serie> findAll();

    Optional<Serie> findById(String id);

}
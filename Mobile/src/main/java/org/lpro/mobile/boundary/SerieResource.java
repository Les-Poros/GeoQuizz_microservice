package org.lpro.mobile.boundary;

import org.lpro.mobile.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;

public interface SerieResource extends CrudRepository<Serie, String> {

    Page<Serie> findAll(Pageable pegeable);

    List<Serie> findAll();

    Optional<Serie> findById(String id);

}
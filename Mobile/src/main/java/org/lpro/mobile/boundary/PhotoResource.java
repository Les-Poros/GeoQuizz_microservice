package org.lpro.mobile.boundary;

import org.lpro.mobile.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;

public interface PhotoResource extends CrudRepository<Photo, String> {

    Page<Photo> findAll(Pageable pegeable);

    List<Photo> findAll();

    Optional<Photo> findById(String id);

    Optional<Photo> findBySerieId(String id);

    Page<Photo> findBySerieId(String id,Pageable pegeable);
}
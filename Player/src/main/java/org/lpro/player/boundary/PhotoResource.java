package org.lpro.player.boundary;

import org.lpro.player.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;

public interface PhotoResource extends CrudRepository<Photo, String> {

    Page<Photo> findAll(Pageable pegeable);

    List<Photo> findAll();

    Optional<Photo> findById(String id);

}
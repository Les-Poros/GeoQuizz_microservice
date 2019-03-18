package org.lpro.backoffice.boundary;

import org.lpro.backoffice.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;
public interface UtilisateurResource extends CrudRepository<Utilisateur, Integer> {
    Page<Utilisateur> findAll(Pageable pegeable);
    List<Utilisateur> findAll();
    Optional<Utilisateur> findByUsername(String username);
}
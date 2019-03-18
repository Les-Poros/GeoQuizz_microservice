
package org.lpro.backoffice.config;

import org.lpro.backoffice.entity.*;

import java.util.Objects;

import org.lpro.backoffice.boundary.UtilisateurResource;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UtilisateurResource  ur;
    @Autowired
    public UserService(UtilisateurResource ur) {
        this.ur = ur;
    }
    
    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        Utilisateur user = ur.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
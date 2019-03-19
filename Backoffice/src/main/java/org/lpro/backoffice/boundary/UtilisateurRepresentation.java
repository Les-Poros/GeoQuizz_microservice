package org.lpro.backoffice.boundary;

import org.springframework.data.domain.*;
import java.util.*;
import org.lpro.backoffice.entity.*;
import org.lpro.backoffice.controller.*;
import org.lpro.backoffice.exception.NotFound;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Utilisateur.class)
public class UtilisateurRepresentation {

    private final UtilisateurResource ur;

    // Injection de dépendances
    public UtilisateurRepresentation(UtilisateurResource ur) {
        this.ur = ur;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(user2Resource(ur.findAll(PageRequest.of(page, size,new Sort(Sort.Direction.ASC, "username")))), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody Utilisateur user) {

        if(ur.findByUsername(user.getUsername())==null){
            throw new NotFound("User deja existant !");
        };
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(new BCryptPasswordEncoder(10).encode(user.getPassword()));
        Utilisateur saved = ur.save(user);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(UtilisateurRepresentation.class).slash(user.getUserId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }


    private Resources<Resource<Utilisateur>> user2Resource(Page<Utilisateur> users) {
        int pageact = users.getPageable().getPageNumber();
        int size = users.getPageable().getPageSize();
        int firstpage = 0;
        int lastpage = users.getTotalPages() - 1;

        if (lastpage == -1)
            lastpage = 0;
        if (pageact > lastpage)
            pageact = lastpage;
        else if (pageact < firstpage)
            pageact = firstpage;
        Link prevLink;
        Link nextLink;

        Link selfLink = new Link(
                linkTo(UtilisateurRepresentation.class) + "?page=" + pageact + "&size=" + size)
                        .withRel("self - page:" + pageact);
        Link firstLink = new Link(linkTo(UtilisateurRepresentation.class) + "?page="
                + firstpage + "&size=" + size).withRel("first - page:" + firstpage);
        Link lastLink = new Link(linkTo(UtilisateurRepresentation.class)+ "?page=" + lastpage
                + "&size=" + size).withRel("last - page:" + lastpage);
        if (pageact >= lastpage)
            nextLink = new Link(linkTo(UtilisateurRepresentation.class) + "?page=" + lastpage
                    + "&size=" + size).withRel("next - page:" + lastpage);
        else
            nextLink = new Link(linkTo(UtilisateurRepresentation.class) + "?page="
                    + (pageact + 1) + "&size=" + size).withRel("next - page:" + (pageact + 1));

        if (pageact <= firstpage)
            prevLink = new Link(linkTo(UtilisateurRepresentation.class) + "?page=" + firstpage
                    + "&size=" + size).withRel("prev - page:" + firstpage);
        else
            prevLink = new Link(linkTo(UtilisateurRepresentation.class) + "?page="
                    + (pageact - 1) + "&size=" + size).withRel("prev - page:" + (pageact - 1));

        List<Resource<Utilisateur>> userResources = new ArrayList();
        users.forEach(user -> userResources.add(userToResource(user)));
        return new Resources<>(userResources, selfLink, firstLink, prevLink, nextLink, lastLink);
    }

    private Resource<Utilisateur> userToResource(Utilisateur user) {

        return new Resource<>(user);

    }
    
}
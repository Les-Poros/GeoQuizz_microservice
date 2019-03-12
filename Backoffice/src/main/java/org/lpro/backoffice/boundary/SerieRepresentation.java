package org.lpro.backoffice.boundary;

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
@RequestMapping(value = "/series", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Serie.class)
public class SerieRepresentation {

    private final PartieResource pr;
    private final PhotoResource phr;
    private final SerieResource sr;

    // Injection de dépendances
    public SerieRepresentation(PartieResource pr,PhotoResource phr,SerieResource sr) {
        this.pr = pr;
        this.phr = phr;
        this.sr = sr;
    }

    @GetMapping
    public ResponseEntity<?> getAllSerie(
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(sr.findAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping(value = "/{serieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSerieAvecId(@PathVariable("serieId") String id) throws NotFound {
        return Optional.ofNullable(sr.findById(id)).filter(Optional::isPresent)
                .map(serie -> new ResponseEntity<>(serie.get(), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Serie inexistante !"));
    }


    @PostMapping
        public ResponseEntity<?> postSerie(@RequestBody Serie serie){
       
            serie.setId(UUID.randomUUID().toString());
            Serie saved = sr.save(serie);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(linkTo(SerieRepresentation.class).slash(saved.getId()).toUri());
            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{serieId}")
    public ResponseEntity<?> putSerie( @PathVariable("serieId") String idserie, @RequestBody Serie serieUpdated) throws NotFound {
        if (!sr.existsById(idserie)) {
            throw new NotFound("Catégorie inexistante !");
        }
        return sr.findById(idserie).map(serie -> {
            serie.setVille(serieUpdated.getVille());
            serie.setMaprefs(serieUpdated.getMaprefs());
            serie.setDist(serieUpdated.getDist());
            serie.setPhoto(serieUpdated.getPhoto());
            serie.setPartie(serieUpdated.getPartie());
            sr.save(serie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("serie inexistant !"));
    }

    @DeleteMapping(value = "/{serieId}")
    public ResponseEntity<?> deleteSerie(
            @PathVariable("serieId") String idserie) throws NotFound {
        return sr.findById(idserie).map(serie -> {
            sr.delete(serie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("serie inexistant !"));
    }


        @GetMapping("/{serieId}/photos")
        public ResponseEntity<?> getSeriePhoto(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @PathVariable("serieId") String id) {
            if (!sr.existsById(id)) {
                throw new NotFound("Serie inexistante !");
            }
            return new ResponseEntity<>(phr.findBySerieId(id,PageRequest.of(page, size)), HttpStatus.OK);
        }

        @PostMapping("/{serieId}/photos")
        public ResponseEntity<?> postPhoto(
        @RequestBody Photo photo,
        @PathVariable("serieId") String idSerie){
            if (!sr.existsById(idSerie)) {
                throw new NotFound("Serie inexistante !");
            }
            photo.setId(UUID.randomUUID().toString());
            photo.setSerie(sr.findById(idSerie).get());
            Photo saved = phr.save(photo);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos").slash(saved.getId()).toUri());
            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    
        }    
    
        
}
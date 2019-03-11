package org.lpro.backoffice.boundary;

import java.util.*;
import org.lpro.backoffice.entity.*;
import org.lpro.backoffice.exception.NotFound;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.*;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/series", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Serie.class)
public class SerieRepresentation {

    private final SerieResource pr;
    private final PhotoResource phr;
    private final SerieResource sr;

    // Injection de dépendances
    public SerieRepresentation(SerieResource pr,PhotoResource phr,SerieResource sr) {
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

    @PostMapping
        public ResponseEntity<?> postSandwich(@RequestBody Serie serie){
       
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
        return pr.findById(idserie).map(serie -> {
            serie.setVille(serieUpdated.getVille());
            serie.setMaprefs(serieUpdated.getMaprefs());
            serie.setDist(serieUpdated.getDist());
            serie.setPhoto(serieUpdated.getPhoto());
            serie.setPartie(serieUpdated.getPartie());
            pr.save(serie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("serie inexistant !"));
    }

    @DeleteMapping(value = "/{serieId}")
    public ResponseEntity<?> deleteSerie(
            @PathVariable("serieId") String idserie) throws NotFound {
        return pr.findById(idserie).map(serie -> {
            pr.delete(serie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("serie inexistant !"));
    }

}
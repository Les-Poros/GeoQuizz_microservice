package org.lpro.player.boundary;

import java.util.*;
import io.jsonwebtoken.*;
import org.lpro.player.entity.*;
import org.lpro.player.exception.NotFound;
import org.lpro.player.exception.MethodNotAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.*;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/parties", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Partie.class)
public class PartieRepresentation {

    private final PartieResource pr;
    private final PhotoResource phr;
    private final SerieResource sr;

    // Injection de dépendances
    public PartieRepresentation(PartieResource pr,PhotoResource phr,SerieResource sr) {
        this.pr = pr;
        this.phr = phr;
        this.sr = sr;
    }

    @GetMapping
    public ResponseEntity<?> getAllPartie(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
            return new ResponseEntity<>(pr.findAll(PageRequest.of(page, limit)), HttpStatus.OK);
    }

    @GetMapping(value = "/{partieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPartieAvecId(@PathVariable("partieId") String id,@RequestHeader(value = "x-lbs-token", required = false, defaultValue = "") String tokenHeader) throws NotFound {
        
        if (!pr.existsById(id)) {
            throw new NotFound("Partie inexistante !");
        }
        Optional<Partie> partie = pr.findById(id);
        if (partie.get().getToken().equals(tokenHeader)){
            partie.get().getSerie().setPhoto(new HashSet<>());
            return new ResponseEntity<>(partie, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("{\"erreur\":\"Token invalide\"}", HttpStatus.FORBIDDEN);
    }

    @PostMapping("/series/{idSerie}")
    public ResponseEntity<?> postPartie(@PathVariable("idSerie") String idSerie, @RequestBody Partie partie){
        if (!sr.existsById(idSerie)) {
            throw new NotFound("Serie inexistante !");
        }
            partie.setId(UUID.randomUUID().toString());
            String token = Jwts.builder().setSubject(partie.getId()).signWith(SignatureAlgorithm.HS256, "secret")
                    .compact();
            partie.setToken(token);
            partie.setScore(0);
            partie.setStatus("start");
            Serie serie=sr.findById(idSerie).get();
            partie.setSerie(serie);
            List<Photo> shufflePhoto = new ArrayList<>(serie.getPhoto());
            Collections.shuffle(shufflePhoto);
            if(partie.getNbphotos()==null)
                partie.setNbphotos(10);
            if(partie.getNbphotos()>=shufflePhoto.size())
                partie.setNbphotos(shufflePhoto.size());
            shufflePhoto.subList(0, partie.getNbphotos()).forEach(photo ->{
                System.out.println(photo.getDescr());
                partie.addPhoto(photo);
            } );
            Partie saved = pr.save(partie);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(linkTo(PartieRepresentation.class).slash(saved.getId()).toUri());
            return new ResponseEntity<>("{\"id\":\""+saved.getId()+"\",\"token\":\""+saved.getToken()+"\"}", responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{partieId}")
    public ResponseEntity<?> putPartie(@PathVariable("partieId") String idpartie, @RequestBody Partie partieUpdated)
            throws NotFound {
        if (!pr.existsById(idpartie)) {
            throw new NotFound("Partie inexistante !");
        }
        return pr.findById(idpartie).map(partie -> {
            partie.setStatus(partieUpdated.getStatus());
            partie.setScore(partieUpdated.getScore());
            pr.save(partie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("partie inexistant !"));
    }

    @DeleteMapping(value = "/{partieId}")
    public ResponseEntity<?> deletePartie(@PathVariable("partieId") String idpartie) throws NotFound {
        return pr.findById(idpartie).map(partie -> {
            pr.delete(partie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("partie inexistant !"));
    }

}
package org.lpro.mobile.boundary;

import org.springframework.data.domain.*;
import java.util.*;
import org.lpro.mobile.entity.*;
import org.lpro.mobile.exception.NotFound;
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
    public SerieRepresentation(PartieResource pr, PhotoResource phr, SerieResource sr) {
        this.pr = pr;
        this.phr = phr;
        this.sr = sr;
    }

    @GetMapping
    public ResponseEntity<?> getAllSerie(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(serie2Resource(sr.findAll(PageRequest.of(page, size,new Sort(Sort.Direction.ASC, "ville")))), HttpStatus.OK);
    }

    @GetMapping(value = "/{serieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSerieAvecId(@PathVariable("serieId") String id) throws NotFound {
        return Optional.ofNullable(sr.findById(id)).filter(Optional::isPresent)
                .map(serie -> new ResponseEntity<>(serieToResource(serie.get(), false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Serie inexistante !"));
    }

    @PostMapping
    public ResponseEntity<?> postSerie(@RequestBody Serie serie) {

        serie.setId(UUID.randomUUID().toString());
        Serie saved = sr.save(serie);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(SerieRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{serieId}")
    public ResponseEntity<?> putSerie(@PathVariable("serieId") String idserie, @RequestBody Serie serieUpdated)
            throws NotFound {
        if (!sr.existsById(idserie)) {
            throw new NotFound("Serie inexistante !");
        }
        Serie serie = sr.findById(idserie).get();
        serie.setVille(serieUpdated.getVille());
        serie.setMap_lon(serieUpdated.getMap_lon());
        serie.setMap_lat(serieUpdated.getMap_lat());
        serie.setDist(serieUpdated.getDist());
        serie.setPhoto(serieUpdated.getPhoto());
        serie.setPartie(serieUpdated.getPartie());
        Serie saved = sr.save(serie);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(SerieRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{serieId}")
    public ResponseEntity<?> deleteSerie(@PathVariable("serieId") String idserie) throws NotFound {
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
        return new ResponseEntity<>(photo2Resource(phr.findBySerieId(id, PageRequest.of(page, size,new Sort(Sort.Direction.ASC, "descr"))), id),
                HttpStatus.OK);
    }

    @PostMapping("/{serieId}/photos")
    public ResponseEntity<?> postPhotos(@RequestBody Photo photo, @PathVariable("serieId") String idSerie) {
        if (!sr.existsById(idSerie)) {
            throw new NotFound("Serie inexistante !");
        }
        photo.setId(UUID.randomUUID().toString());
        photo.setSerie(sr.findById(idSerie).get());
        Photo saved = phr.save(photo);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
                linkTo(SerieRepresentation.class).slash(idSerie).slash("photos").slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @GetMapping("/{serieId}/photos/{photoId}")
    public ResponseEntity<?> getSeriePhoto(@PathVariable("serieId") String serieId,
            @PathVariable("photoId") String photoId) {
        if (!sr.existsById(serieId)) {
            throw new NotFound("Serie inexistante !");
        }
        return Optional.ofNullable(phr.findById(photoId)).filter(Optional::isPresent)
                .map(photo -> new ResponseEntity<>(photo.get(), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Serie inexistante !"));
    }

    @PutMapping(value = "/{serieId}/photos/{photoId}")
    public ResponseEntity<?> putPhoto(@PathVariable("serieId") String idserie, @PathVariable("photoId") String photoId,
            @RequestBody Photo photoUpdated) throws NotFound {
        if (!sr.existsById(idserie)) {
            throw new NotFound("Serie inexistante !");
        }
        if (!phr.existsById(photoId)) {
            throw new NotFound("Photo inexistante !");
        }
        Photo photo = phr.findById(photoId).get();
        photo.setDescr(photoUpdated.getDescr());
        photo.setLatitude(photoUpdated.getLatitude());
        photo.setLongitude(photoUpdated.getLongitude());
        photo.setUrl(photoUpdated.getUrl());
        Photo saved = phr.save(photo);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
                linkTo(SerieRepresentation.class).slash(idserie).slash("photos").slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    private Resources<Resource<Serie>> serie2Resource(Page<Serie> series) {
        int pageact = series.getPageable().getPageNumber();
        int size = series.getPageable().getPageSize();
        int firstpage = 0;
        int lastpage = series.getTotalPages() - 1;
        if (lastpage == -1)
            lastpage = 0;
        if (pageact > lastpage)
            pageact = lastpage;
        else if (pageact < firstpage)
            pageact = firstpage;
        Link prevLink;
        Link nextLink;

        Link selfLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + pageact + "&size=" + size)
                .withRel("self - page:" + pageact);
        Link firstLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + firstpage + "&size=" + size)
                .withRel("first - page:" + firstpage);
        Link lastLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + lastpage + "&size=" + size)
                .withRel("last - page:" + lastpage);
        if (pageact >= lastpage)
            nextLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + lastpage + "&size=" + size)
                    .withRel("next - page:" + lastpage);
        else
            nextLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + (pageact + 1) + "&size=" + size)
                    .withRel("next - page:" + (pageact + 1));

        if (pageact <= firstpage)
            prevLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + firstpage + "&size=" + size)
                    .withRel("prev - page:" + firstpage);
        else
            prevLink = new Link(linkTo(SerieRepresentation.class) + "?page=" + (pageact - 1) + "&size=" + size)
                    .withRel("prev - page:" + (pageact - 1));

        List<Resource<Serie>> serieResources = new ArrayList();
        series.forEach(serie -> serieResources.add(serieToResource(serie, false)));
        return new Resources<>(serieResources, selfLink, firstLink, prevLink, nextLink, lastLink);
    }

    private Resource<Serie> serieToResource(Serie serie, Boolean collection) {
        Link selfLink = linkTo(SerieRepresentation.class).slash(serie.getId()).withSelfRel();
        Link photoLink = linkTo(SerieRepresentation.class).slash(serie.getId()).slash("photos").withRel("photos");
        if (collection) {
            Link collectionLink = linkTo(SerieRepresentation.class).withRel("collection");
            return new Resource<>(serie, photoLink, selfLink, collectionLink);
        } else {
            return new Resource<>(serie, photoLink, selfLink);
        }
    }

    private Resources<Resource<Photo>> photo2Resource(Page<Photo> photos, String idSerie) {
        int pageact = photos.getPageable().getPageNumber();
        int size = photos.getPageable().getPageSize();
        int firstpage = 0;
        int lastpage = photos.getTotalPages() - 1;

        if (lastpage == -1)
            lastpage = 0;
        if (pageact > lastpage)
            pageact = lastpage;
        else if (pageact < firstpage)
            pageact = firstpage;
        Link prevLink;
        Link nextLink;

        Link selfLink = new Link(
                linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page=" + pageact + "&size=" + size)
                        .withRel("self - page:" + pageact);
        Link firstLink = new Link(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page="
                + firstpage + "&size=" + size).withRel("first - page:" + firstpage);
        Link lastLink = new Link(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page=" + lastpage
                + "&size=" + size).withRel("last - page:" + lastpage);
        if (pageact >= lastpage)
            nextLink = new Link(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page=" + lastpage
                    + "&size=" + size).withRel("next - page:" + lastpage);
        else
            nextLink = new Link(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page="
                    + (pageact + 1) + "&size=" + size).withRel("next - page:" + (pageact + 1));

        if (pageact <= firstpage)
            prevLink = new Link(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page=" + firstpage
                    + "&size=" + size).withRel("prev - page:" + firstpage);
        else
            prevLink = new Link(linkTo(SerieRepresentation.class).slash(idSerie).slash("photos") + "?page="
                    + (pageact - 1) + "&size=" + size).withRel("prev - page:" + (pageact - 1));

        List<Resource<Photo>> photoResources = new ArrayList();
        photos.forEach(photo -> photoResources.add(photoToResource(photo)));
        return new Resources<>(photoResources, selfLink, firstLink, prevLink, nextLink, lastLink);
    }

    private Resource<Photo> photoToResource(Photo photo) {

        return new Resource<>(photo);

    }
}
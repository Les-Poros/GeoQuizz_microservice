package org.lpro.backoffice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lpro.backoffice.entity.*;
import org.lpro.backoffice.boundary.*;
import org.lpro.backoffice.exception.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
public class RestImageController {

    private final Logger logger = LoggerFactory.getLogger(SerieRepresentation.class);
    private static String UPLOADED_FOLDER = "./img/";

    private final PartieResource pr;
    private final PhotoResource phr;
    private final SerieResource sr;

    // Injection de dépendances
    public RestImageController(PartieResource pr, PhotoResource phr, SerieResource sr) {
        this.pr = pr;
        this.phr = phr;
        this.sr = sr;
    }

    @PutMapping("series/{serieId}/photos/{photoId}/image")
    public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile uploadimage,
            @PathVariable("serieId") String idSerie, @PathVariable("photoId") String idPhoto) {
        if (!sr.existsById(idSerie)) {
            throw new NotFound("Serie inexistante !");
        }
        if (!phr.existsById(idPhoto)) {
            throw new NotFound("Photo inexistante !");
        }

        logger.debug("image upload!");

        if (uploadimage.isEmpty()) {
            return new ResponseEntity("No image", HttpStatus.BAD_REQUEST);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadimage));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return phr.findById(idPhoto).map(photo -> {
            photo.setUrl(uploadimage.getOriginalFilename());
            phr.save(photo);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("serie inexistant !"));
    }

    @GetMapping(value = "img/{lienImage}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> uploadFile(@PathVariable("lienImage") String lienImage) throws IOException {
       
        Path path = Paths.get(UPLOADED_FOLDER +lienImage);
        byte[] bytes=Files.readAllBytes(path);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

    private void saveUploadedFiles(List<MultipartFile> images) throws IOException {

        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }
            byte[] bytes = image.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());
            Files.write(path, bytes);
        }

    }

}

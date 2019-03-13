package org.lpro.player.entity;

import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Partie {

    @Id
    private String id;
    private String token;
    private Integer nbphotos;
    private String status;
    private Integer score;
    private String joueur;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "partie_photo",
        joinColumns = @JoinColumn(name = "partie_id"),
        inverseJoinColumns = @JoinColumn(name = "photo_id"))
    private Set<Photo> photo = new HashSet<>();;

    Partie() {
        // necessaire pour JPA !
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addPhoto(Photo photo) {
        this.photo.add(photo);
        photo.getPartie().add(this);
    }

    public Serie getSerie() {
        return this.serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Set<Photo> getPhoto() {
        return this.photo;
    }

    public void setPhoto(Set<Photo> photo) {
        this.photo = photo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return this.score;
    }
  
    public void setJoueur(String joueur) {
        this.joueur = joueur;
    }

    public String getJoueur() {
        return this.joueur;
    }  
    
    public void setNbphotos(Integer nbphotos) {
        this.nbphotos = nbphotos;
    }

    public Integer getNbphotos() {
        return this.nbphotos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partie)) return false;
        return id != null && id.equals(((Partie) o).id);
    }
 
    @Override
    public int hashCode() {
        return 31;
    }
}
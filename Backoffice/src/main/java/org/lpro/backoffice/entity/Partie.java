package org.lpro.backoffice.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

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
    @JsonIgnore
    private Serie serie;

    @OneToMany(mappedBy = "partie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Photo> photo;

    Partie() {
        // necessaire pour JPA !
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
}
package org.lpro.player.entity;

import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Serie {

    @Id
    private String id;
    private String ville;
    private String map_lat;
    private String map_lon;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Photo> photo = new HashSet<>();;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Partie> partie = new HashSet<>();;

    Serie() {
        // necessaire pour JPA !
    }

    public String getMap_lon() {
        return map_lon;
    }

    public void setMap_lon(String map_lon) {
        this.map_lon = map_lon;
    }

    public String getMap_lat() {
        return map_lat;
    }

    public void setMap_lat(String map_lat) {
        this.map_lat = map_lat;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Photo> getPhoto() {
        return this.photo;
    }

    public void setPhoto(Set<Photo> photo) {
        this.photo = photo;
    }

    public Set<Partie> getPartie() {
        return this.partie;
    }

    public void setPartie(Set<Partie> partie) {
        this.partie = partie;
    }
    
    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getVille() {
        return this.ville;
    }
  
}
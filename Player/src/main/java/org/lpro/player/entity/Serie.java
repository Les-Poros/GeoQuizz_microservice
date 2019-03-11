package org.lpro.player.entity;

import javax.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Serie {

    @Id
    private String id;
    private String ville;
    private String maprefs;
    private String dist;
    
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Photo> photo;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Partie> partie;

    Serie() {
        // necessaire pour JPA !
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
    
    public void setMaprefs(String maprefs) {
        this.maprefs = maprefs;
    }

    public String getMaprefs() {
        return this.maprefs;
    }
  
    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getDist() {
        return this.dist;
    }  
}
package org.lpro.mobile.entity;

import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Photo {

    @Id
    private String id;
    private String descr;
    private String latitude;
    private String longitude;
    private String url;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    @JsonIgnore
    private Serie serie;

    @ManyToMany(mappedBy = "photo")
    @JsonIgnore
    private Set<Partie> partie = new HashSet<>();
    Photo() {
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
    public Set<Partie> getPartie() {
        return this.partie;
    }

    public void setPartie(Set<Partie> partie ) {
        this.partie = partie;
    }
    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo tag = (Photo) o;
        return Objects.equals(id, tag.id);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
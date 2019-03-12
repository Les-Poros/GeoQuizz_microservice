package org.lpro.backoffice.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

@Entity
public class Photo {

    @Id
    private String id;
    private String descr;
    private String position;
    private String url;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    @JsonIgnore
    private Serie serie;

    @ManyToOne
    @JoinColumn(name = "partie_id", nullable = true)
    @JsonIgnore
    private Partie partie;


    public Photo() {
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
    public Partie getPartie() {
        return this.partie;
    }

    public void setPartie(Partie partie ) {
        this.partie = partie;
    }
    public String getDescr() {
        return this.descr;
    }

    public void setDesc(String descr) {
        this.descr = descr;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }





}
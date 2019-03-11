package org.lpro.player.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Photo {

    @Id
    private String id;
    private String desc;
    private String position;
    private String url;

    @ManyToOne(nullable=false)
    @JsonIgnore
    private Serie serie;

    @ManyToOne(mappedBy = "Photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, nullable=true)
    private Set<Partie> partie;


    Photo() {
        // necessaire pour JPA !
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Uselog.
 */
@Entity
@Table(name = "uselog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Uselog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_id")
    private Long name_id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "count")
    private Integer count;

    @Column(name = "username")
    private String username;

    @Column(name = "jhi_date")
    private Instant date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getName_id() {
        return name_id;
    }

    public Uselog name_id(Long name_id) {
        this.name_id = name_id;
        return this;
    }

    public void setName_id(Long name_id) {
        this.name_id = name_id;
    }

    public String getName() {
        return name;
    }

    public Uselog name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public Uselog type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public Uselog count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUsername() {
        return username;
    }

    public Uselog username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getDate() {
        return date;
    }

    public Uselog date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Uselog uselog = (Uselog) o;
        if (uselog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), uselog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Uselog{" +
            "id=" + getId() +
            ", name_id='" + getName_id() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", count='" + getCount() + "'" +
            ", username='" + getUsername() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}

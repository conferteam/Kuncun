package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Alarm.
 */
@Entity
@Table(name = "alarm")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "reserves", precision=10, scale=2)
    private BigDecimal reserves;

    @Column(name = "jhi_limit", precision=10, scale=2)
    private BigDecimal limit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Alarm name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getReserves() {
        return reserves;
    }

    public Alarm reserves(BigDecimal reserves) {
        this.reserves = reserves;
        return this;
    }

    public void setReserves(BigDecimal reserves) {
        this.reserves = reserves;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public Alarm limit(BigDecimal limit) {
        this.limit = limit;
        return this;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alarm alarm = (Alarm) o;
        if (alarm.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alarm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Alarm{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", reserves='" + getReserves() + "'" +
            ", limit='" + getLimit() + "'" +
            "}";
    }
}

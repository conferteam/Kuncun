package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Type.
 */
@Entity
@Table(name = "type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Type implements Serializable {

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

    @Column(name = "del_flag")
    private Integer del_flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Type name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getReserves() {
        return reserves;
    }

    public Type reserves(BigDecimal reserves) {
        this.reserves = reserves;
        return this;
    }

    public void setReserves(BigDecimal reserves) {
        this.reserves = reserves;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public Type limit(BigDecimal limit) {
        this.limit = limit;
        return this;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public Integer getDel_flag() {
        return del_flag;
    }

    public Type del_flag(Integer del_flag) {
        this.del_flag = del_flag;
        return this;
    }

    public void setDel_flag(Integer del_flag) {
        this.del_flag = del_flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Type type = (Type) o;
        if (type.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), type.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Type{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", reserves='" + getReserves() + "'" +
            ", limit='" + getLimit() + "'" +
            ", del_flag='" + getDel_flag() + "'" +
            "}";
    }
}

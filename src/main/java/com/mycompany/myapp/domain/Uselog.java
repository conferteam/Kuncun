package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A UseLog.
 */
@Entity
@Table(name = "use_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
public class UseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_id")
    private Long nameId;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_type")
    private Integer type;

    @CreatedBy
    @Column(name = "username")
    private String username;

    @CreatedDate
    @Column(name = "jhi_date")
    private Instant date = Instant.now();

    @Column(name = "count", precision=10, scale=2)
    private BigDecimal count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNameId() {
        return nameId;
    }

    public UseLog nameId(Long nameId) {
        this.nameId = nameId;
        return this;
    }

    public void setNameId(Long nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public UseLog name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public UseLog type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public UseLog username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getDate() {
        return date;
    }

    public UseLog date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getCount() {
        return count;
    }

    public UseLog count(BigDecimal count) {
        this.count = count;
        return this;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UseLog useLog = (UseLog) o;
        if (useLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), useLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UseLog{" +
            "id=" + getId() +
            ", nameId='" + getNameId() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", username='" + getUsername() + "'" +
            ", date='" + getDate() + "'" +
            ", count='" + getCount() + "'" +
            "}";
    }
}

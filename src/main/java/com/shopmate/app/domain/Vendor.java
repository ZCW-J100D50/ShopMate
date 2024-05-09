package com.shopmate.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vendor.
 */
@Entity
@Table(name = "vendor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendor")
    @JsonIgnoreProperties(value = { "items", "vendor", "userProfile" }, allowSetters = true)
    private Set<Trip> trips = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vendor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Vendor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return this.city;
    }

    public Vendor city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Trip> getTrips() {
        return this.trips;
    }

    public void setTrips(Set<Trip> trips) {
        if (this.trips != null) {
            this.trips.forEach(i -> i.setVendor(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setVendor(this));
        }
        this.trips = trips;
    }

    public Vendor trips(Set<Trip> trips) {
        this.setTrips(trips);
        return this;
    }

    public Vendor addTrips(Trip trip) {
        this.trips.add(trip);
        trip.setVendor(this);
        return this;
    }

    public Vendor removeTrips(Trip trip) {
        this.trips.remove(trip);
        trip.setVendor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendor)) {
            return false;
        }
        return getId() != null && getId().equals(((Vendor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vendor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}

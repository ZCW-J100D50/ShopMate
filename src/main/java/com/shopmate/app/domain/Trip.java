package com.shopmate.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trips")
    @JsonIgnoreProperties(value = { "trips" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trips" }, allowSetters = true)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "user", "trips" }, allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Trip name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBudget() {
        return this.budget;
    }

    public Trip budget(Double budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Trip date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setTrips(null));
        }
        if (items != null) {
            items.forEach(i -> i.setTrips(this));
        }
        this.items = items;
    }

    public Trip items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Trip addItems(Item item) {
        this.items.add(item);
        item.setTrips(this);
        return this;
    }

    public Trip removeItems(Item item) {
        this.items.remove(item);
        item.setTrips(null);
        return this;
    }

    public Vendor getVendor() {
        return this.vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Trip vendor(Vendor vendor) {
        this.setVendor(vendor);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Trip userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return getId() != null && getId().equals(((Trip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", budget=" + getBudget() +
            ", date='" + getDate() + "'" +
            "}";
    }
}

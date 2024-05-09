package com.shopmate.app.web.rest;

import com.shopmate.app.domain.Trip;
import com.shopmate.app.repository.TripRepository;
import com.shopmate.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.shopmate.app.domain.Trip}.
 */
@RestController
@RequestMapping("/api/trips")
@Transactional
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripRepository tripRepository;

    public TripResource(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * {@code POST  /trips} : Create a new trip.
     *
     * @param trip the trip to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trip, or with status {@code 400 (Bad Request)} if the trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", trip);
        if (trip.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trip = tripRepository.save(trip);
        return ResponseEntity.created(new URI("/api/trips/" + trip.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trip.getId().toString()))
            .body(trip);
    }

    /**
     * {@code PUT  /trips/:id} : Updates an existing trip.
     *
     * @param id the id of the trip to save.
     * @param trip the trip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trip,
     * or with status {@code 400 (Bad Request)} if the trip is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trip trip)
        throws URISyntaxException {
        log.debug("REST request to update Trip : {}, {}", id, trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trip = tripRepository.save(trip);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trip.getId().toString()))
            .body(trip);
    }

    /**
     * {@code PATCH  /trips/:id} : Partial updates given fields of an existing trip, field will ignore if it is null
     *
     * @param id the id of the trip to save.
     * @param trip the trip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trip,
     * or with status {@code 400 (Bad Request)} if the trip is not valid,
     * or with status {@code 404 (Not Found)} if the trip is not found,
     * or with status {@code 500 (Internal Server Error)} if the trip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trip> partialUpdateTrip(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trip trip)
        throws URISyntaxException {
        log.debug("REST request to partial update Trip partially : {}, {}", id, trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trip> result = tripRepository
            .findById(trip.getId())
            .map(existingTrip -> {
                if (trip.getName() != null) {
                    existingTrip.setName(trip.getName());
                }
                if (trip.getBudget() != null) {
                    existingTrip.setBudget(trip.getBudget());
                }
                if (trip.getDate() != null) {
                    existingTrip.setDate(trip.getDate());
                }

                return existingTrip;
            })
            .map(tripRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trip.getId().toString())
        );
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trips in body.
     */
    @GetMapping("")
    public List<Trip> getAllTrips() {
        log.debug("REST request to get all Trips");
        return tripRepository.findAll();
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     *
     * @param id the id of the trip to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trip, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable("id") Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<Trip> trip = tripRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trip);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     *
     * @param id the id of the trip to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable("id") Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

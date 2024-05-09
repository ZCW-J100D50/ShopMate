package com.shopmate.app.web.rest;

import static com.shopmate.app.domain.TripAsserts.*;
import static com.shopmate.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopmate.app.IntegrationTest;
import com.shopmate.app.domain.Trip;
import com.shopmate.app.repository.TripRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TripResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_BUDGET = 1D;
    private static final Double UPDATED_BUDGET = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip().name(DEFAULT_NAME).budget(DEFAULT_BUDGET).date(DEFAULT_DATE);
        return trip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity(EntityManager em) {
        Trip trip = new Trip().name(UPDATED_NAME).budget(UPDATED_BUDGET).date(UPDATED_DATE);
        return trip;
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity(em);
    }

    @Test
    @Transactional
    void createTrip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trip
        var returnedTrip = om.readValue(
            restTripMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trip)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Trip.class
        );

        // Validate the Trip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTripUpdatableFieldsEquals(returnedTrip, getPersistedTrip(returnedTrip));
    }

    @Test
    @Transactional
    void createTripWithExistingId() throws Exception {
        // Create the Trip with an existing ID
        trip.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc
            .perform(get(ENTITY_API_URL_ID, trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip.name(UPDATED_NAME).budget(UPDATED_BUDGET).date(UPDATED_DATE);

        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTripToMatchAllProperties(updatedTrip);
    }

    @Test
    @Transactional
    void putNonExistingTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL_ID, trip.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip.name(UPDATED_NAME);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrip, trip), getPersistedTrip(trip));
    }

    @Test
    @Transactional
    void fullUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip.name(UPDATED_NAME).budget(UPDATED_BUDGET).date(UPDATED_DATE);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripUpdatableFieldsEquals(partialUpdatedTrip, getPersistedTrip(partialUpdatedTrip));
    }

    @Test
    @Transactional
    void patchNonExistingTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL_ID, trip.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trip
        restTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, trip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tripRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Trip getPersistedTrip(Trip trip) {
        return tripRepository.findById(trip.getId()).orElseThrow();
    }

    protected void assertPersistedTripToMatchAllProperties(Trip expectedTrip) {
        assertTripAllPropertiesEquals(expectedTrip, getPersistedTrip(expectedTrip));
    }

    protected void assertPersistedTripToMatchUpdatableProperties(Trip expectedTrip) {
        assertTripAllUpdatablePropertiesEquals(expectedTrip, getPersistedTrip(expectedTrip));
    }
}

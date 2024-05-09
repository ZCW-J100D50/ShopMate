package com.shopmate.app.domain;

import static com.shopmate.app.domain.ItemTestSamples.*;
import static com.shopmate.app.domain.TripTestSamples.*;
import static com.shopmate.app.domain.UserProfileTestSamples.*;
import static com.shopmate.app.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.shopmate.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TripTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trip.class);
        Trip trip1 = getTripSample1();
        Trip trip2 = new Trip();
        assertThat(trip1).isNotEqualTo(trip2);

        trip2.setId(trip1.getId());
        assertThat(trip1).isEqualTo(trip2);

        trip2 = getTripSample2();
        assertThat(trip1).isNotEqualTo(trip2);
    }

    @Test
    void itemsTest() throws Exception {
        Trip trip = getTripRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        trip.addItems(itemBack);
        assertThat(trip.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getTrips()).isEqualTo(trip);

        trip.removeItems(itemBack);
        assertThat(trip.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getTrips()).isNull();

        trip.items(new HashSet<>(Set.of(itemBack)));
        assertThat(trip.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getTrips()).isEqualTo(trip);

        trip.setItems(new HashSet<>());
        assertThat(trip.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getTrips()).isNull();
    }

    @Test
    void vendorTest() throws Exception {
        Trip trip = getTripRandomSampleGenerator();
        Vendor vendorBack = getVendorRandomSampleGenerator();

        trip.setVendor(vendorBack);
        assertThat(trip.getVendor()).isEqualTo(vendorBack);

        trip.vendor(null);
        assertThat(trip.getVendor()).isNull();
    }

    @Test
    void userProfileTest() throws Exception {
        Trip trip = getTripRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        trip.setUserProfile(userProfileBack);
        assertThat(trip.getUserProfile()).isEqualTo(userProfileBack);

        trip.userProfile(null);
        assertThat(trip.getUserProfile()).isNull();
    }
}

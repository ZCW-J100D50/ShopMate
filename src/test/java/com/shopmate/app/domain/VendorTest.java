package com.shopmate.app.domain;

import static com.shopmate.app.domain.TripTestSamples.*;
import static com.shopmate.app.domain.VendorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.shopmate.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VendorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendor.class);
        Vendor vendor1 = getVendorSample1();
        Vendor vendor2 = new Vendor();
        assertThat(vendor1).isNotEqualTo(vendor2);

        vendor2.setId(vendor1.getId());
        assertThat(vendor1).isEqualTo(vendor2);

        vendor2 = getVendorSample2();
        assertThat(vendor1).isNotEqualTo(vendor2);
    }

    @Test
    void tripsTest() throws Exception {
        Vendor vendor = getVendorRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        vendor.addTrips(tripBack);
        assertThat(vendor.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getVendor()).isEqualTo(vendor);

        vendor.removeTrips(tripBack);
        assertThat(vendor.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getVendor()).isNull();

        vendor.trips(new HashSet<>(Set.of(tripBack)));
        assertThat(vendor.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getVendor()).isEqualTo(vendor);

        vendor.setTrips(new HashSet<>());
        assertThat(vendor.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getVendor()).isNull();
    }
}

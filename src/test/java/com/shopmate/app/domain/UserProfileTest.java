package com.shopmate.app.domain;

import static com.shopmate.app.domain.TripTestSamples.*;
import static com.shopmate.app.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.shopmate.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void tripsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        userProfile.addTrips(tripBack);
        assertThat(userProfile.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeTrips(tripBack);
        assertThat(userProfile.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getUserProfile()).isNull();

        userProfile.trips(new HashSet<>(Set.of(tripBack)));
        assertThat(userProfile.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setTrips(new HashSet<>());
        assertThat(userProfile.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getUserProfile()).isNull();
    }
}

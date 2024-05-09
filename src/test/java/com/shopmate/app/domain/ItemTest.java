package com.shopmate.app.domain;

import static com.shopmate.app.domain.ItemTestSamples.*;
import static com.shopmate.app.domain.TripTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.shopmate.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = getItemSample1();
        Item item2 = new Item();
        assertThat(item1).isNotEqualTo(item2);

        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);

        item2 = getItemSample2();
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void tripsTest() throws Exception {
        Item item = getItemRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        item.setTrips(tripBack);
        assertThat(item.getTrips()).isEqualTo(tripBack);

        item.trips(null);
        assertThat(item.getTrips()).isNull();
    }
}

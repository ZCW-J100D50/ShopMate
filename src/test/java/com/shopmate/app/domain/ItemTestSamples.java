package com.shopmate.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Item getItemSample1() {
        return new Item().id(1L).name("name1").quantity(1);
    }

    public static Item getItemSample2() {
        return new Item().id(2L).name("name2").quantity(2);
    }

    public static Item getItemRandomSampleGenerator() {
        return new Item().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).quantity(intCount.incrementAndGet());
    }
}

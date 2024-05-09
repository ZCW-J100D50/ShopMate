package com.shopmate.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VendorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vendor getVendorSample1() {
        return new Vendor().id(1L).name("name1").city("city1");
    }

    public static Vendor getVendorSample2() {
        return new Vendor().id(2L).name("name2").city("city2");
    }

    public static Vendor getVendorRandomSampleGenerator() {
        return new Vendor().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).city(UUID.randomUUID().toString());
    }
}

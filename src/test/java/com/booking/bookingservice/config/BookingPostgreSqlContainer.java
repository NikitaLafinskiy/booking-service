package com.booking.bookingservice.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class BookingPostgreSqlContainer extends PostgreSQLContainer<BookingPostgreSqlContainer> {
    public static final String IMAGE_VERSION = "postgres:11.1";
    public static final String DB_URL = "DB_URL";
    public static final String DB_USERNAME = "DB_USERNAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";

    private static BookingPostgreSqlContainer instance;

    private BookingPostgreSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BookingPostgreSqlContainer getInstance() {
        if (instance == null) {
            instance = new BookingPostgreSqlContainer();
        }

        return instance;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty(DB_URL, instance.getJdbcUrl());
        System.setProperty(DB_USERNAME, instance.getUsername());
        System.setProperty(DB_PASSWORD, instance.getPassword());
    }

    @Override
    public void stop() {

    }
}

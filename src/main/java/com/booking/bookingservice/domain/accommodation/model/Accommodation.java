package com.booking.bookingservice.domain.accommodation.model;

import com.booking.bookingservice.domain.booking.model.Booking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accommodation")
@Getter
@Setter
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AccommodationType type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String size;

    @OneToMany(mappedBy = "accommodation",
            cascade = CascadeType.ALL)
    private List<Amenity> amenities = new ArrayList<>();

    @OneToMany(mappedBy = "accommodation",
            cascade = CascadeType.REMOVE)
    private List<Booking> bookings = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal dailyRate;

    @Column(nullable = false)
    private Integer availability;

    public enum AccommodationType {
        HOUSE,
        APARTMENT,
        CONDO,
        VACATION_HOME
    }
}

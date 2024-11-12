package com.booking.bookingservice.domain.accommodation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    @OneToMany(mappedBy = "belongsTo")
    private List<Amenity> amenities;

    @Column(nullable = false)
    private BigDecimal dailyRate;

    @Column(nullable = false)
    private Integer availability;

    private enum AccommodationType {
        HOUSE,
        APARTMENT,
        CONDO,
        VACATION_HOME
    }
}

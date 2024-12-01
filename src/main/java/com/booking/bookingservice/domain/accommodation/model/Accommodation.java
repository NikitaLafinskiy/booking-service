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
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accommodation")
@Getter
@Setter
@NoArgsConstructor
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
    private Set<Amenity> amenities = new HashSet<>();

    @OneToMany(mappedBy = "accommodation",
            cascade = CascadeType.REMOVE)
    private Set<Booking> bookings = new HashSet<>();

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

    public Accommodation(Long id) {
        this.id = id;
    }
}

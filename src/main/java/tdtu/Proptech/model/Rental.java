package tdtu.Proptech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "realtor_id", nullable = false)
    private User realtor;

    @Column(nullable = false)
    private LocalDateTime rentalDate;

    @Column(nullable = false)
    private Integer rentalDuration;

    @Column(nullable = false)
    private Double rentalPrice;

    @Column(nullable = false)
    private String renterName;

    @Column(nullable = false, unique = true)
    private String renterEmail;

    @Column(nullable = false)
    private String renterPhone;

    public Rental(Property property, User realtor, LocalDateTime rentalDate, Integer rentalDuration, Double rentalPrice, String renterName, String renterEmail, String renterPhone) {
        this.property = property;
        this.realtor = realtor;
        this.rentalDate = rentalDate;
        this.rentalDuration = rentalDuration;
        this.rentalPrice = rentalPrice;
        this.renterName = renterName;
        this.renterEmail = renterEmail;
        this.renterPhone = renterPhone;
    }
}

package tdtu.Proptech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
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
}

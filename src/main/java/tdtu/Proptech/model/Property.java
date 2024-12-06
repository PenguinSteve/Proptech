package tdtu.Proptech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private Double salesPrice;

    @Column(nullable = true)
    private Double rentalPrice;

    @Column(nullable = false)
    private String type; // SALE, RENT

    @Column(nullable = false)
    private String status; // AVAILABLE, SOLD, RENTED

    @OneToMany(mappedBy = "properties", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}


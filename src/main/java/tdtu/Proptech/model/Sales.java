package tdtu.Proptech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "sales")
public class Sales {
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
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private Double salePrice;

    @Column(nullable = false)
    private String buyerName;

    @Column(nullable = false)
    private String buyerEmail;

    @Column(nullable = false)
    private String buyerPhone;

    public Sales(Property property, User realtor, LocalDateTime saleDate, Double salePrice, String buyerName, String buyerEmail, String buyerPhone) {
        this.property = property;
        this.realtor = realtor;
        this.saleDate = saleDate;
        this.salePrice = salePrice;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerPhone = buyerPhone;
    }}

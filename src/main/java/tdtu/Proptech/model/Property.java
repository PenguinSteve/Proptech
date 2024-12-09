package tdtu.Proptech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private Double price;

    private String area;

    private String description;

    @Column(nullable = false)
    private String type; // SALES, RENTAL

    @Column(nullable = false)
    private String status; // AVAILABLE, SOLD, RENTED

    @Column
    private LocalDateTime expire;

    @ManyToOne
    @JoinColumn(name = "realtor_id", nullable = false)
    private User realtor;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Property (String name, String address, Double price, String area, String description, String type, String status, User realtor, LocalDateTime expire){
        this.name = name;
        this.address = address;
        this.price = price;
        this.area = area;
        this.description = description;
        this.type = type;
        this.status = status;
        this.realtor = realtor;
        this.expire = expire;
    }
}


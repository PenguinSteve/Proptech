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
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String role;

    @Column
    private String imageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = true)
    private Subscription subscription;

    @Column
    private LocalDateTime expireSubscription;

    public boolean isSubscriptionActive() {
        if (this.subscription == null || this.expireSubscription == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(this.expireSubscription);
    }

    @OneToMany(mappedBy = "realtor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;

    @OneToMany(mappedBy = "realtor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals;

    @JsonIgnore
    @OneToMany(mappedBy = "realtor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties;

    @OneToMany(mappedBy = "realtor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}



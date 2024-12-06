package tdtu.Proptech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = true)
    private Subscription subscription;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    public boolean isSubscriptionActive() {
        if (this.subscription == null || this.endDate == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(this.endDate);
    }

    @OneToMany(mappedBy = "realtor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;

    @OneToMany(mappedBy = "realtor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals;
}



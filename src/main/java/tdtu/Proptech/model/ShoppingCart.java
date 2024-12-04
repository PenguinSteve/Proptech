package tdtu.Proptech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private LocalDateTime createdAt;

    private Double totalAmount;


    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setShoppingCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item){
        this.cartItems.remove(item);
        item.setShoppingCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(0.0, Double::sum);
    }
}


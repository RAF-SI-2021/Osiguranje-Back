package buyingmarket.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Actuary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Boolean active;
    @OneToMany
    private Collection<Order> orders;

    public Actuary() {
        this.orders = new HashSet<>();
    }

    public Actuary(Long userId) {
        this.userId = userId;
        this.active = true;
        this.orders = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }
}

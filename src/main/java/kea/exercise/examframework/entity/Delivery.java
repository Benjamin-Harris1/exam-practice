package kea.exercise.examframework.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate deliveryDate;
    private String fromWareHouse;
    private String destination;

    @OneToMany(mappedBy = "delivery", cascade = {CascadeType.ALL})
    private List<ProductOrder> productOrders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "van_id")
    private Van van;

    private boolean isActive;

    public Delivery(LocalDate deliveryDate, String fromWareHouse, String destination) {
        this.deliveryDate = deliveryDate;
        this.fromWareHouse = fromWareHouse;
        this.destination = destination;
    }
}

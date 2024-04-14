package kea.exercise.examframework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Delivery delivery;

    public ProductOrder(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

}


package kea.exercise.examframework.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrderDTO {
    private int id;
    private int quantity;
    private int productId;
}

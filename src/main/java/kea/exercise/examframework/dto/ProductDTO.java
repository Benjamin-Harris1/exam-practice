package kea.exercise.examframework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private int id;
    private String name;
    private int price;
    private int weight;
    private boolean isActive;
}

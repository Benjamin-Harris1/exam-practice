package kea.exercise.examframework.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VanDTO {
    private int id;
    private String brand;
    private String model;
    private int capacity;
    private int remainingCapacity;
    private List<DeliveryDTO> deliveries;
}

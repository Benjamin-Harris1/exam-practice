package kea.exercise.examframework.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryDTO {
    private int id;
    private LocalDate deliveryDate;
    private String fromWareHouse;
    private String destination;
}

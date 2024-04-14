package kea.exercise.examframework.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class DeliveryDTO {
    private int id;
    private LocalDate deliveryDate;
    private String fromWareHouse;
    private String destination;
    private List<ProductOrderDTO> productOrders = new ArrayList<>();
}

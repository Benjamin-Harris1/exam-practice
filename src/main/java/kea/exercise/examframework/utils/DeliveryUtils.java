package kea.exercise.examframework.utils;

import kea.exercise.examframework.entity.Delivery;

public class DeliveryUtils {

    public static double calculateTotalWeight(Delivery delivery) {
        return delivery.getProductOrders().stream()
                .mapToDouble(productOrder -> productOrder.getProduct().getWeight() * productOrder.getQuantity())
                .sum();
    }
}
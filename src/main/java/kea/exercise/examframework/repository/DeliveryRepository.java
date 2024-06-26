package kea.exercise.examframework.repository;

import kea.exercise.examframework.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    List<Delivery> findAllByVanId(int vanId);
    List<Delivery> findAllByIsActiveTrue();
}

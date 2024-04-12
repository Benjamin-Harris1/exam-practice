package kea.exercise.examframework.repository;

import kea.exercise.examframework.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer>{
}

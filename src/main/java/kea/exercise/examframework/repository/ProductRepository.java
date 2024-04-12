package kea.exercise.examframework.repository;

import kea.exercise.examframework.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>{
}

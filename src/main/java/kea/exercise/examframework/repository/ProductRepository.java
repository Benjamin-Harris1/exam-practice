package kea.exercise.examframework.repository;

import kea.exercise.examframework.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    List<Product> findAllByIsActiveTrue();
}

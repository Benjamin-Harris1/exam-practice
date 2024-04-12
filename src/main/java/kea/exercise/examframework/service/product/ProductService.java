package kea.exercise.examframework.service.product;

import kea.exercise.examframework.dto.ProductDTO;
import kea.exercise.examframework.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO findById(int id);
    ProductDTO create(ProductDTO productDTO);
    ProductDTO update(int id, ProductDTO productDTO);
    void deleteProduct(int id);
}

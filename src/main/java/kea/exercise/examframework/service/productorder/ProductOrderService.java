package kea.exercise.examframework.service.productorder;

import kea.exercise.examframework.dto.ProductOrderDTO;

import java.util.List;

public interface ProductOrderService {
    List<ProductOrderDTO> findAll();
    ProductOrderDTO findById(int id);
    ProductOrderDTO create(ProductOrderDTO productOrderDTO);
    ProductOrderDTO update(int id, ProductOrderDTO productOrderDTO);
    void deleteProductOrder(int id);

}

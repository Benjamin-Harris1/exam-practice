package kea.exercise.examframework.service.productorder;

import kea.exercise.examframework.dto.ProductOrderDTO;
import kea.exercise.examframework.entity.ProductOrder;

import java.util.List;

public interface ProductOrderService {
    List<ProductOrderDTO> findAll();
    ProductOrderDTO findById(int id);
    ProductOrderDTO create(ProductOrderDTO productOrderDTO);
    ProductOrderDTO update(int id, ProductOrderDTO productOrderDTO);
    void deleteProductOrder(int id);
    ProductOrder convertToEntity(ProductOrderDTO productOrderDTO);
    ProductOrderDTO convertToDTO(ProductOrder productOrder);
    void updateFromDTO(ProductOrder productOrder, ProductOrderDTO productOrderDTO);



}

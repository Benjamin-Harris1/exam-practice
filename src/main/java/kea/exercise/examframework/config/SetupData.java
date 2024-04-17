package kea.exercise.examframework.config;

import kea.exercise.examframework.dto.ProductDTO;
import kea.exercise.examframework.entity.Product;
import kea.exercise.examframework.service.delivery.DeliveryService;
import kea.exercise.examframework.service.product.ProductService;

import java.util.Arrays;
import java.util.List;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SetupData implements ApplicationRunner {

    private final ProductService productService;
    private final DeliveryService deliveryService;

    public SetupData(ProductService productService, DeliveryService deliveryService) {
        this.productService = productService;
        this.deliveryService = deliveryService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initData();
    }

    private void initData() {

        List<Product> products = Arrays.asList(
            new Product("Product 1", 10, 10),
            new Product("Product 2", 15, 20),
            new Product("Product 3", 30, 200)
        );
        products.forEach(product -> {
            ProductDTO productDTO = productService.create(productService.convertToDTO(product));
            product.setId(productDTO.getId());
        });

    }


}

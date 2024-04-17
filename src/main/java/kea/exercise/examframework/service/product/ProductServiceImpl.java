package kea.exercise.examframework.service.product;

import kea.exercise.examframework.dto.ProductDTO;
import kea.exercise.examframework.entity.Product;
import kea.exercise.examframework.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.text.html.Option;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAllByIsActiveTrue().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(int id) {
        Optional<Product> product = productRepository.findById(id)
        .filter(Product::isActive);
        return product.map(this::convertToDTO).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public ProductDTO findByName(String name) {
        Optional<Product> product = productRepository.findByName(name)
        .filter(Product::isActive);
        return product.map(this::convertToDTO).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        product.setActive(true);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO update(int id, ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setWeight(productDTO.getWeight());
            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        }
        throw new RuntimeException("Product not found");
    }

    @Override
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setWeight(product.getWeight());
        dto.setActive(product.isActive());

        return dto;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setActive(productDTO.isActive());

        return product;
    }
}

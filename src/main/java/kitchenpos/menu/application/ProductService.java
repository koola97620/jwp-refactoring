package kitchenpos.menu.application;

import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        return ProductResponse.of(productRepository.save(productRequest.toEntity()));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }
}

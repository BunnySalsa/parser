package application.controllers;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

import team.microchad.api.ProductApi;
import team.microchad.api.dto.ProductBlankDto;
import team.microchad.api.dto.ProductDto;
import team.microchad.api.dto.ProductDtoResponse;
import domain.model.Product.ProductId;
import domain.operations.CreateProductOperation;
import domain.operations.GetProductOperation;
import domain.operations.UpdateProductOperation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {
  private final CreateProductOperation createProduct;
  private final GetProductOperation getProduct;
  private final UpdateProductOperation updateProductOperation;
  private final ControllersMapper mapper;

  @Override
  public ResponseEntity<ProductDtoResponse> createProduct(ProductBlankDto dto) {
    var blank = mapper.map(dto);
    var product = createProduct.execute(blank);
    var response = mapper.map(product);
    return status(OK).body(response);
  }

  @Override
  public ResponseEntity<ProductDtoResponse> getProductById(UUID productId) {
    var id = new ProductId(productId);
    var product = getProduct.execute(id);
    var response = mapper.map(product);
    return status(OK).body(response);
  }

  @Override
  public ResponseEntity<Void> updateProductById(UUID productId, ProductDto productDto) {
    var id = new ProductId(productId);
    var product = mapper.map(productDto);
    updateProductOperation.execute(id, product);

    return status(OK).build();
  }
}

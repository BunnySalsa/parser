package application.controllers;


import domain.model.Product;
import domain.model.ProductBlank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import team.microchad.api.dto.ProductBlankDto;
import team.microchad.api.dto.ProductDto;
import team.microchad.api.dto.ProductDtoResponse;

@Mapper(componentModel = "spring")
public interface ControllersMapper {

  default ProductBlank map(ProductBlankDto dto) {
    return ProductBlank.builder()
        .title(dto.getTitle())
        .build();
  }

  default ProductDtoResponse map(Product product) {
    var dto = new ProductDto();
    dto.setId(product.getId().getValue());
    dto.setTitle(product.getTitle());
    dto.setCreated(product.getCreated());
    var response = new ProductDtoResponse();
    response.setData(dto);

    return response;
  }

  @Mapping(target = "id", expression = "java(Product.ProductId.of(productDto.getId()))")
  Product map(ProductDto productDto);
}

package com.acsalamancaf.dscatalog.factories;

import java.time.Instant;
import java.util.Set;

import com.acsalamancaf.dscatalog.entities.Category;
import com.acsalamancaf.dscatalog.entities.Product;
import com.acsalamancaf.dscatalogdto.ProductDTO;

public class ProductFactory {

	@SuppressWarnings("unchecked")
	public static Product createdProduct() {
		Category category = new Category(2L,"Eletronics", Instant.parse("2021-03-27T20:50:07.12345Z"), Instant.parse("2022-07-13T20:50:07.12345Z"));
		Product product = new Product(1L,"Phone", "Descrption", 89.0, "http://teste.com",
				Instant.parse("2020-07-13T20:50:07.12345Z"), Set.of(category));
	//	product.getCategories().add(new Category(2L,"Eletronics", null, null));
		return product;
	}
	
	public static ProductDTO createdProductDto() {
		Product product = createdProduct();
		return new ProductDTO(product, product.getCategories());
	}
}

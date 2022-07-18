package com.acsalamancaf.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.acsalamancaf.dscatalog.entities.Product;
import com.acsalamancaf.dscatalog.factories.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long expected;
	private long notExpected;
	private long countTotalProduct;
	
	@BeforeEach
    void setUp() throws Exception{
		long expected = 1L;
		long notExpected = 1000L;
		long countTotalProduct =25L;
	}
	
	@Test
	public void saveShouldPersistWhithAutoIncrementWhenIdIsNull() {
		Product product = ProductFactory.createdProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
//		Assertions.assertEquals(countTotalProduct + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		
		repository.deleteById(expected + 1);
		
		Optional<Product> result = repository.findById(expected);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShoulThrowsEmptyResultDataAccesExcepctionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(notExpected);
		});
	}
//
//	
//	implementar os seguintes testes em ProductRepositoryTests:
//		findById deveria 
//		retornar um Optional<Product> não vazio quando o id existir
//		retornar um Optional<Product> vazio quando o id não existir

	@Test
	public void findByIdShouldObjectWhenIdExist() {
		
		Optional<Product> result = repository.findById(expected + 2);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldObjectWhenIdNotExist() {
		
		Optional<Product> result = repository.findById(expected + 1000);
		
		Assertions.assertFalse(result.isPresent());
	}
}

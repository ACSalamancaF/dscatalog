package com.acsalamancaf.dscatalog.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.acsalamancaf.dscatalog.entities.Product;
import com.acsalamancaf.dscatalog.factories.ProductFactory;
import com.acsalamancaf.dscatalog.repositories.ProductRepository;
import com.acsalamancaf.dscatalog.services.ProductService;
import com.acsalamancaf.dscatalog.services.exceptions.DataBaseException;
import com.acsalamancaf.dscatalog.services.exceptions.ResourceNotFoundException;
import com.acsalamancaf.dscatalogdto.ProductDTO;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;	
	
	@Mock
	private ProductRepository repository;
	
	private long existId;
	private long notExistId;
	private long dependenceId;
	private PageImpl<Product> page;
	private Product product;

	@BeforeEach
	void setUp() throws Exception{
		existId= 1L;
		notExistId =2L;
		dependenceId= 3L;
		product = ProductFactory.createdProduct();
		page = new PageImpl<>(List.of(product));
		
		//comportamento simulado com mokito
		
		//qdo returna objeto
		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page); //testar findall page
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product); //testar save product
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(product)); //testar encontrar por Id product
		Mockito.when(repository.findById(notExistId)).thenReturn(Optional.empty()); //testar Id não existe product
		
		//do void
		Mockito.doNothing().when(repository).deleteById(existId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(notExistId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependenceId);
	}
	@Test
	public  void findAllPagedShouldReturnPage() {
	
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPage(pageable);
	
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}
	
	
	@Test
	public void deleteShoulThrowDataBaseExceptionWhenDepedenceIdExist() {
		
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependenceId);
		});
		
		Mockito.verify(repository).deleteById(dependenceId);
	}
	
	@Test
	public void deleteShoulThrowResourceNotFoundExceptionWhenIdNonExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(notExistId);
		});
		
		Mockito.verify(repository).deleteById(notExistId);
	}
	
	@Test
	public void deleteShoulDoNotingWhenIdExist() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existId);
	}
	
}

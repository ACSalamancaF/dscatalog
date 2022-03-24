package com.acsalamancaf.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acsalamancaf.dscatalog.entities.Category;
import com.acsalamancaf.dscatalog.entities.Product;
import com.acsalamancaf.dscatalog.repositories.CategoryRepository;
import com.acsalamancaf.dscatalog.repositories.ProductRepository;
import com.acsalamancaf.dscatalog.services.exceptions.DataBaseException;
import com.acsalamancaf.dscatalog.services.exceptions.ResourceNotFoundException;
import com.acsalamancaf.dscatalogdto.CategoryDTO;
import com.acsalamancaf.dscatalogdto.ProductDTO;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<ProductDTO> findAll() {
		List<Product> list = productRepository.findAll();
		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = productRepository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = productRepository.save(entity);

		return new ProductDTO(entity);
	}

	

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {

			Product entity = productRepository.getById(id);
			copyDtoToEntity(dto, entity);
			entity = productRepository.save(entity);
			return new ProductDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+ id); 
		}
		catch(DataIntegrityViolationException e){
			throw new DataBaseException("Dabase exception");
		}
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPage(PageRequest pageRequest) {
		Page<Product> list = productRepository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}
	
	@SuppressWarnings("deprecation")
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategoriesDto()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}

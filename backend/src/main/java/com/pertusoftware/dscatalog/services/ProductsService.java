package com.pertusoftware.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pertusoftware.dscatalog.dto.CategoryDTO;
import com.pertusoftware.dscatalog.dto.ProductDTO;
import com.pertusoftware.dscatalog.entities.Category;
import com.pertusoftware.dscatalog.entities.Product;
import com.pertusoftware.dscatalog.repositories.CategoryRepository;
import com.pertusoftware.dscatalog.repositories.ProductRepository;
import com.pertusoftware.dscatalog.services.exceptions.DatabaseException;
import com.pertusoftware.dscatalog.services.exceptions.ResourceNotFoundException;

/*registra a classe como um componente que participa do sistema de injeção de dependência do Spring*/
@Service
public class ProductsService {
	@Autowired
	private ProductRepository reposotory;
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, PageRequest pageRequest) {
		List<Category> categories = (categoryId==0) ? null: Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> list = reposotory.find(categories, name, pageRequest);

		// Expressão lambda
		return list.map(x -> new ProductDTO(x));

//		Método alternatido e mais antigo
//		List <ProductDTO> listDto = new ArrayList<>();
//		for(Product cat : list) {
//			listDto.add(new ProductDTO(cat));
//		}
//		return listDto;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = reposotory.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = reposotory.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = reposotory.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = reposotory.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}

	}

	public void delete(Long id) {
		try {
			reposotory.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}

	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}

package com.pertusoftware.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pertusoftware.dscatalog.dto.ProductDTO;
import com.pertusoftware.dscatalog.entities.Product;
import com.pertusoftware.dscatalog.repositories.ProductRepository;
import com.pertusoftware.dscatalog.services.exceptions.DatabaseException;
import com.pertusoftware.dscatalog.services.exceptions.ResourceNotFoundException;

/*registra a classe como um componente que participa do sistema de injeção de dependência do Spring*/
@Service
public class ProductsService {
	@Autowired
	private ProductRepository reposotory;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = reposotory.findAll(pageRequest);

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
		//entity.setName(dto.getName()
		entity = reposotory.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = reposotory.getOne(id);
			//entity.setName(dto.getName());
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
}

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

import com.pertusoftware.dscatalog.dto.CategoryDTO;
import com.pertusoftware.dscatalog.entities.Category;
import com.pertusoftware.dscatalog.repositories.CategoryRepository;
import com.pertusoftware.dscatalog.services.exceptions.DatabaseException;
import com.pertusoftware.dscatalog.services.exceptions.ResourceNotFoundException;

/*registra a classe como um componente que participa do sistema de injeção de dependência do Spring*/
@Service
public class CategorysService {
	@Autowired
	private CategoryRepository reposotory;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<Category> list = reposotory.findAll(pageRequest);

		// Expressão lambda
		return list.map(x -> new CategoryDTO(x));

//		Método alternatido e mais antigo
//		List <CategoryDTO> listDto = new ArrayList<>();
//		for(Category cat : list) {
//			listDto.add(new CategoryDTO(cat));
//		}
//		return listDto;
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = reposotory.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = reposotory.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = reposotory.getOne(id);
			entity.setName(dto.getName());
			entity = reposotory.save(entity);
			return new CategoryDTO(entity);
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

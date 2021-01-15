package com.pertusoftware.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pertusoftware.dscatalog.dto.CategoryDTO;
import com.pertusoftware.dscatalog.entities.Category;
import com.pertusoftware.dscatalog.repositories.CategoryRepository;
import com.pertusoftware.dscatalog.services.exceptions.EntityNotFoundException;

/*registra a classe como um componente que participa do sistema de injeção de dependência do Spring*/
@Service
public class CategorysService {
	@Autowired
	private CategoryRepository reposotory;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = reposotory.findAll();

		// Expressão lambda
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

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
		Category entity = obj.orElseThrow(()-> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}
}

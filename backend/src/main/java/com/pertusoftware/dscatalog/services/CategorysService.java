package com.pertusoftware.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pertusoftware.dscatalog.entities.Category;
import com.pertusoftware.dscatalog.repositories.CategoryRepository;

/*registra a classe como um componente que participa do sistema de injeção de dependência do Spring*/
@Service
public class CategorysService {
	@Autowired
	private CategoryRepository reposotory;

	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return reposotory.findAll();
	}
}

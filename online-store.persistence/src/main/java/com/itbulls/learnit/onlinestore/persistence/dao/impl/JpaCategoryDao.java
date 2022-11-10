package com.itbulls.learnit.onlinestore.persistence.dao.impl;

import java.util.List;

import com.itbulls.learnit.onlinestore.persistence.dao.CategoryDao;
import com.itbulls.learnit.onlinestore.persistence.dto.CategoryDto;

import jakarta.persistence.Persistence;

public class JpaCategoryDao implements CategoryDao {

	@Override
	public CategoryDto getCategoryByCategoryId(int id) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			CategoryDto category = em.find(CategoryDto.class, id);
			em.getTransaction().commit();
			return category;
		}
	}

	@Override
	public List<CategoryDto> getCategories() {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			List<CategoryDto> categories = em.createQuery("SELECT c FROM category c", CategoryDto.class).getResultList();
			em.getTransaction().commit();
			return categories;
		}
	}

}

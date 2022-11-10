package com.itbulls.learnit.onlinestore.persistence.dao.impl;

import com.itbulls.learnit.onlinestore.persistence.dao.PurchaseStatusDao;
import com.itbulls.learnit.onlinestore.persistence.dto.PurchaseStatusDto;

import jakarta.persistence.Persistence;

public class JpaPurchaseStatusDao implements PurchaseStatusDao {

	@Override
	public PurchaseStatusDto getPurchaseStatusById(Integer id) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			PurchaseStatusDto purchaseStatus = em.find(PurchaseStatusDto.class, id);
			
			em.getTransaction().commit();
			
			return purchaseStatus;
		}
	}

}

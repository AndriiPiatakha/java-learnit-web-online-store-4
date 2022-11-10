package com.itbulls.learnit.onlinestore.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.itbulls.learnit.onlinestore.persistence.dao.PurchaseDao;
import com.itbulls.learnit.onlinestore.persistence.dto.PurchaseDto;
import com.itbulls.learnit.onlinestore.persistence.dto.PurchaseStatusDto;
import com.itbulls.learnit.onlinestore.persistence.dto.UserDto;

import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class JpaPurchaseDao implements PurchaseDao {

	@Override
	public void savePurchase(PurchaseDto order) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			em.persist(order);
			
			em.getTransaction().commit();
		}
	}

	@Override
	public List<PurchaseDto> getPurchasesByUserId(int userId) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			TypedQuery<PurchaseDto> query = em.createQuery("SELECT p FROM purchase p WHERE p.userDto.id = :id", PurchaseDto.class);
			query.setParameter("id", userId);
			
			List<PurchaseDto> resultList = query.getResultList();
			em.getTransaction().commit();
			
			return resultList;
		}
	}

	@Override
	public List<PurchaseDto> getPurchases() {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			TypedQuery<PurchaseDto> query = em.createQuery("SELECT p FROM purchase p", PurchaseDto.class);
			
			List<PurchaseDto> resultList = query.getResultList();
			em.getTransaction().commit();
			
			return resultList;
		}
	}

	@Override
	public List<PurchaseDto> getNotCompletedPurchases(Integer lastFulfilmentStageId) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			Query query = em.createQuery("SELECT p.id, p.userDto, p.purchaseStatusDto FROM purchase p WHERE p.purchaseStatusDto.id != :statusId");
			query.setParameter("statusId", lastFulfilmentStageId);
			
			List<Object[]> resultList = query.getResultList();
			List<PurchaseDto> purchases = new ArrayList<>();
			for (Object[] resultTuple : resultList) {
				purchases.add(new PurchaseDto(
						(Integer)resultTuple[0], 
						(UserDto)resultTuple[1], 
						(PurchaseStatusDto)resultTuple[2]));
			}
			
			em.getTransaction().commit();
			
			return purchases;
		}
	}

	@Override
	public PurchaseDto getPurchaseById(Integer purchaseId) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<PurchaseDto> criteriaQuery = criteriaBuilder.createQuery(PurchaseDto.class);
			Root<PurchaseDto> purchaseRoot = criteriaQuery.from(PurchaseDto.class);
			purchaseRoot.fetch("userDto");
			purchaseRoot.fetch("productDtos");
			Query query = em.createQuery(criteriaQuery.select(purchaseRoot).where(criteriaBuilder.equal(purchaseRoot.get("id"), purchaseId)));
			
			PurchaseDto purchase = (PurchaseDto)query.getSingleResult();
			em.getTransaction().commit();
			
			return purchase;
		}
	}

	@Override
	public void updatePurchase(PurchaseDto newPurchase) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			em.merge(newPurchase);
			em.getTransaction().commit();
		}
	}

}

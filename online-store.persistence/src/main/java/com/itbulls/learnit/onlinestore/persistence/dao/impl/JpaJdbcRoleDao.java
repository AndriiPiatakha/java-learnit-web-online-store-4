package com.itbulls.learnit.onlinestore.persistence.dao.impl;

import com.itbulls.learnit.onlinestore.persistence.dao.RoleDao;
import com.itbulls.learnit.onlinestore.persistence.dto.PurchaseStatusDto;
import com.itbulls.learnit.onlinestore.persistence.dto.RoleDto;

import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class JpaJdbcRoleDao implements RoleDao {

	@Override
	public RoleDto getRoleById(int id) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			RoleDto role = em.find(RoleDto.class, id);
			
			em.getTransaction().commit();
			
			return role;
		}
	}

	@Override
	public RoleDto getRoleByRoleName(String roleName) {
		try(var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager();) {
			em.getTransaction().begin();
			
			TypedQuery<RoleDto> query = em.createQuery("SELECT r FROM role r WHERE r.name = :roleName", RoleDto.class);
			query.setParameter("roleName", roleName);
			
			RoleDto role = query.getSingleResult();
			
			em.getTransaction().commit();
			
			return role;
		}
	}

}

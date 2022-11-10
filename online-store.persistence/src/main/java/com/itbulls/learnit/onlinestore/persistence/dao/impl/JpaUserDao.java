package com.itbulls.learnit.onlinestore.persistence.dao.impl;

import java.util.List;

import com.itbulls.learnit.onlinestore.persistence.dao.UserDao;
import com.itbulls.learnit.onlinestore.persistence.dto.UserDto;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class JpaUserDao implements UserDao {

	@Override
	public boolean saveUser(UserDto user) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			em.merge(user);
			
			em.getTransaction().commit();
			return true;
		}
	}

	@Override
	public List<UserDto> getUsers() {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			List<UserDto> users = em.createQuery("SELECT u FROM user u", UserDto.class).getResultList();
			
			em.getTransaction().commit();
			return users;
		}
	}

	@Override
	public UserDto getUserByEmail(String userEmail) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			TypedQuery<UserDto> query = em.createQuery("SELECT u FROM user u WHERE u.email = :email", UserDto.class);
			query.setParameter("email", userEmail);
			try {
				UserDto user = query.getSingleResult();
				em.getTransaction().commit();
				return user;
			} catch (NoResultException e) {
				return null;
			}
		}
	}

	@Override
	public UserDto getUserById(int id) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			UserDto user = em.find(UserDto.class, id);
			
			em.getTransaction().commit();
			return user;
		}
	}

	@Override
	public UserDto getUserByPartnerCode(String partnerCode) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			System.out.println(partnerCode);
			TypedQuery<UserDto> query = em.createQuery("SELECT u FROM user u WHERE u.partnerCode = :partnerCode", UserDto.class);
			query.setParameter("partnerCode", partnerCode);
			
			try {
				UserDto user = query.getSingleResult();
				em.getTransaction().commit();
				return user;
			} catch (NoResultException e) {
				return null;
			}
			
		}
	}

	@Override
	public void updateUser(UserDto newUser) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			em.merge(newUser);
			
			em.getTransaction().commit();
		}
	}

	@Override
	public List<UserDto> getReferralsByUserId(int id) {
		try (var emf = Persistence.createEntityManagerFactory("persistence-unit");
				var em = emf.createEntityManager()) {
			em.getTransaction().begin();
			
			TypedQuery<UserDto> query = em.createQuery("SELECT u FROM user u WHERE u.referrerUser.id = :id", UserDto.class);
			query.setParameter("id", id);
			
			List<UserDto> users = query.getResultList();
			em.getTransaction().commit();
			return users;
		}
	}

}

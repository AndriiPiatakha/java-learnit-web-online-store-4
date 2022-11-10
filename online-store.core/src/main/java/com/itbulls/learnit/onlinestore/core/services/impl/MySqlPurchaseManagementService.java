package com.itbulls.learnit.onlinestore.core.services.impl;

import java.util.List;

import com.itbulls.learnit.onlinestore.core.services.PurchaseManagementService;
import com.itbulls.learnit.onlinestore.persistence.dao.PurchaseDao;
import com.itbulls.learnit.onlinestore.persistence.dao.impl.JpaPurchaseDao;
import com.itbulls.learnit.onlinestore.persistence.dto.PurchaseDto;
import com.itbulls.learnit.onlinestore.persistence.dto.converters.PurchaseDtoToPurchaseConverter;
import com.itbulls.learnit.onlinestore.persistence.entities.Purchase;

public class MySqlPurchaseManagementService implements PurchaseManagementService {
	
	private PurchaseDao purchaseDao;
	private PurchaseDtoToPurchaseConverter purchaseConverter;
	
	{
		purchaseDao = new JpaPurchaseDao();
		purchaseConverter = new PurchaseDtoToPurchaseConverter();
	}

	@Override
	public void addPurchase(Purchase purchase) {
		purchaseDao.savePurchase(purchaseConverter.convertPurchaseToPurchaseDto(purchase));
	}

	@Override
	public List<Purchase> getPurchasesByUserId(int userId) {
		List<PurchaseDto> purchasesDtos = purchaseDao.getPurchasesByUserId(userId);
		return purchaseConverter.convertPurchaseDtosToPurchases(purchasesDtos);
	}

	@Override
	public List<Purchase> getPurchases() {
		List<PurchaseDto> purchasesDtos = purchaseDao.getPurchases();
		return purchaseConverter.convertPurchaseDtosToPurchases(purchasesDtos);
	}

}

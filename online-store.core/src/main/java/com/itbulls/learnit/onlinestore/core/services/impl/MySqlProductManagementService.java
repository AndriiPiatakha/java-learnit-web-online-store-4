package com.itbulls.learnit.onlinestore.core.services.impl;

import java.util.List;

import com.itbulls.learnit.onlinestore.core.services.ProductManagementService;
import com.itbulls.learnit.onlinestore.persistence.dao.ProductDao;
import com.itbulls.learnit.onlinestore.persistence.dao.impl.JpaProductDao;
import com.itbulls.learnit.onlinestore.persistence.dto.ProductDto;
import com.itbulls.learnit.onlinestore.persistence.dto.converters.ProductDtoToProductConverter;
import com.itbulls.learnit.onlinestore.persistence.entities.Product;

public class MySqlProductManagementService implements ProductManagementService {

	private ProductDao productDao;
	private ProductDtoToProductConverter productConverter;
	
	{
		productDao = new JpaProductDao();
		productConverter = new ProductDtoToProductConverter();
	}
	
	@Override
	public List<Product> getProducts() {
		List<ProductDto> productDtos = productDao.getProducts();
		return productConverter.convertProductDtosToProducts(productDtos);
	}

	@Override
	public Product getProductById(int id) {
		ProductDto productDto = productDao.getProductById(id);
		return productConverter.convertProductDtoToProduct(productDto);
	}

}

package com.itbulls.learnit.onlinestore.persistence.dto;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "purchase")
public class PurchaseDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "fk_purchase_user")
	private UserDto userDto;
	
	@ManyToMany
	@JoinTable(name="purchase_product",
	   joinColumns=@JoinColumn(name="purchase_id"),
	   inverseJoinColumns=@JoinColumn(name="product_id"))
	private List<ProductDto> productDtos;
	
	@ManyToOne
	@JoinColumn(name = "fk_purchase_purchase_status")
	private PurchaseStatusDto purchaseStatusDto;
	
	@Column(name = "purchase_timestamp", insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date purchaseTimestamp;
	
	public PurchaseDto() {
	}
	
	public PurchaseDto(Integer id, UserDto userDto, PurchaseStatusDto purchaseStatusDto) {
		this.id = id;
		this.userDto = userDto;
		this.purchaseStatusDto = purchaseStatusDto;
	}

	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
	public List<ProductDto> getProductDtos() {
		return productDtos;
	}
	public void setProductDtos(List<ProductDto> productDtos) {
		this.productDtos = productDtos;
	}
	public PurchaseStatusDto getPurchaseStatusDto() {
		return this.purchaseStatusDto;
	}
	public void setPurchaseStatusDto(PurchaseStatusDto purchaseStatusDto) {
		this.purchaseStatusDto = purchaseStatusDto;
	}
	public Date getPurchaseTimestamp() {
		return purchaseTimestamp;
	}
	public void setPurchaseTimestamp(Date purchaseTimestamp) {
		this.purchaseTimestamp = purchaseTimestamp;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}

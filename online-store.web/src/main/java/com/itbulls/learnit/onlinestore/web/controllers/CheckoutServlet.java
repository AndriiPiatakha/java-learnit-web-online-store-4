package com.itbulls.learnit.onlinestore.web.controllers;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ResourceBundle;

import com.itbulls.learnit.onlinestore.core.facades.ProductFacade;
import com.itbulls.learnit.onlinestore.core.facades.PurchaseFacade;
import com.itbulls.learnit.onlinestore.core.facades.impl.DefaultProductFacade;
import com.itbulls.learnit.onlinestore.core.facades.impl.DefaultPurchaseFacade;
import com.itbulls.learnit.onlinestore.persistence.entities.User;
import com.itbulls.learnit.onlinestore.web.Configurations;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

	private PurchaseFacade purchaseFacade = DefaultPurchaseFacade.getInstance();
	private ProductFacade productFacade = DefaultProductFacade.getInstance();
	private ResourceBundle rb = ResourceBundle.getBundle(Configurations.RESOURCE_BUNDLE_BASE_NAME);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productGuid = request.getParameter("guid");
		purchaseFacade.createPurchase(
				(User) request.getSession().getAttribute(SignInServlet.LOGGED_IN_USER_ATTR),
				productFacade.getProductByGuid(productGuid));

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getServletContext().getContextPath();
		
		request.getSession().setAttribute("orderStatus", rb.getString("order.created.msg"));
		
		response.sendRedirect(baseUrl + "/product?guid=" + productGuid);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}

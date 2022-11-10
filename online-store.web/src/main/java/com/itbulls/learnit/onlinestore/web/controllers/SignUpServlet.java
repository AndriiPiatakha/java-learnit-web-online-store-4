package com.itbulls.learnit.onlinestore.web.controllers;

import static com.itbulls.learnit.onlinestore.web.filters.PartnerCodeFilter.PARTNER_CODE_COOKIE_NAME;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itbulls.learnit.onlinestore.core.facades.UserFacade;
import com.itbulls.learnit.onlinestore.core.facades.impl.DefaultUserFacade;
import com.itbulls.learnit.onlinestore.core.services.Validator;
import com.itbulls.learnit.onlinestore.core.services.impl.PasswordValidator;
import com.itbulls.learnit.onlinestore.persistence.entities.User;
import com.itbulls.learnit.onlinestore.persistence.entities.impl.DefaultUser;
import com.itbulls.learnit.onlinestore.web.Configurations;
import com.itbulls.learnit.onlinestore.web.utils.PBKDF2WithHmacSHA1EncryptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUpServlet.class);
	
	private UserFacade userFacade = DefaultUserFacade.getInstance();
	private Validator passValidator = PasswordValidator.getInstance();
	private ResourceBundle rb = ResourceBundle.getBundle(Configurations.RESOURCE_BUNDLE_BASE_NAME);
	private PBKDF2WithHmacSHA1EncryptionService encryptionService = PBKDF2WithHmacSHA1EncryptionService.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(Configurations.VIEWS_PATH_RESOLVER 
				+ "signup.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String baseUrl = request.getScheme()
			      + "://"
			      + request.getServerName()
			      + ":"
			      + request.getServerPort()
			      + request.getServletContext().getContextPath();
		
		User user = new DefaultUser();
		user.setFirstName(request.getParameter("firstName"));
		user.setLastName(request.getParameter("lastName"));
		user.setEmail(request.getParameter("email"));
		
		String passwordParameter = request.getParameter("password");
		user.setPassword(encryptionService.generatePasswordWithSaltAndHash(passwordParameter));
		
		
		User userByEmail = userFacade.getUserByEmail(user.getEmail());
		
		if (userByEmail != null) {
			request.getSession().setAttribute("errMsg", rb.getString("signup.err.msg.email.exists"));
			response.sendRedirect(baseUrl + "/signup");
			LOGGER.warn("Registration is failed. User with such email {} already exists", user.getEmail());
			return;
		}
		
		if (!passwordParameter.equals(request.getParameter("repeatPassword"))) {
			request.getSession().setAttribute("errMsg", rb.getString("signup.err.msg.repeat.password"));
			response.sendRedirect(baseUrl + "/signup");
			LOGGER.warn("Registration is failed. Repeat password is not correct");
			return;
		}
		
		List<String> errorMessages = passValidator.validate(passwordParameter);
		if (errorMessages.size() != 0) {
			String errMsg = rb.getString("signup.err.msg.general.error");
			if (errorMessages.contains(PasswordValidator.LENGTH_OR_SPECIAL_CHARACTER_ERROR)) {
				errMsg = rb.getString("signup.err.msg.special.character");
				LOGGER.warn("Registration is failed. Password shorter than 8 characters or doesn't contain a special character.");
			}
			if (errorMessages.contains(PasswordValidator.MOST_COMMON_PASSWORD)) {
				errMsg = rb.getString("signup.err.msg.common.password");
				LOGGER.warn("Registration is failed. User selected one of the most common passwords.");
			}
			request.getSession().setAttribute("errMsg", errMsg);
			response.sendRedirect(baseUrl + "/signup");
			return;
		}
		
		String partnerCode = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(PARTNER_CODE_COOKIE_NAME)) {
					partnerCode = cookie.getValue();
					LOGGER.info("Partner code {} is found in cookie", partnerCode);
				}
			}
		}
		
		userFacade.registerUser(user, partnerCode);
		LOGGER.info("User with email {} is registered successfully", user.getEmail());
		response.sendRedirect(baseUrl + "/signin");
	}

}

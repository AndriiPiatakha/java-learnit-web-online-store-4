package com.itbulls.learnit.onlinestore.web.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import com.itbulls.learnit.onlinestore.core.facades.UserFacade;
import com.itbulls.learnit.onlinestore.core.facades.impl.DefaultUserFacade;
import com.itbulls.learnit.onlinestore.core.services.Validator;
import com.itbulls.learnit.onlinestore.core.services.impl.PasswordValidator;
import com.itbulls.learnit.onlinestore.persistence.entities.User;
import com.itbulls.learnit.onlinestore.web.Configurations;
import com.itbulls.learnit.onlinestore.web.utils.PBKDF2WithHmacSHA1EncryptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/edit-profile")
public class EditProfileServlet extends HttpServlet {
	private UserFacade userFacade = DefaultUserFacade.getInstance();
	private Validator passValidator = PasswordValidator.getInstance();
	private ResourceBundle rb = ResourceBundle.getBundle(Configurations.RESOURCE_BUNDLE_BASE_NAME);
	private PBKDF2WithHmacSHA1EncryptionService encryptionService = PBKDF2WithHmacSHA1EncryptionService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO - if not logged in - then redirect to the sign in
		User loggedInUser = (User)request.getSession().getAttribute(SignInServlet.LOGGED_IN_USER_ATTR);
		
		if (loggedInUser == null) {
			String baseUrl = request.getScheme()
				      + "://"
				      + request.getServerName()
				      + ":"
				      + request.getServerPort()
				      + request.getServletContext().getContextPath();
			response.sendRedirect(baseUrl + "/signin");
			
		} else {
			request.getRequestDispatcher(Configurations.VIEWS_PATH_RESOLVER + "editProfile.jsp").forward(request, response);
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String baseUrl = request.getScheme()
			      + "://"
			      + request.getServerName()
			      + ":"
			      + request.getServerPort()
			      + request.getServletContext().getContextPath();
		
		User loggedInUser = (User)request.getSession().getAttribute(SignInServlet.LOGGED_IN_USER_ATTR);
		// need to do this to extract latest state of the user
		User user = userFacade.getUserById(loggedInUser.getId());
		user.setFirstName(request.getParameter("firstName"));
		user.setLastName(request.getParameter("lastName"));
		String emailParameter = request.getParameter("email");
		user.setEmail(emailParameter);
		
		
		User userByEmail = userFacade.getUserByEmail(user.getEmail());
		
		if (userByEmail != null && !emailParameter.equals(loggedInUser.getEmail())) {
			request.getSession().setAttribute("errMsg", rb.getString("signup.err.msg.email.exists"));
			response.sendRedirect(baseUrl + "/edit-profile");
			return;
		}
		
		if (!encryptionService.validatePassword(request.getParameter("password"), loggedInUser.getPassword())) {
			request.getSession().setAttribute("errMsg", rb.getString("signup.err.msg.old.password.wrong"));
			response.sendRedirect(baseUrl + "/edit-profile");
			return;
		}
		
		String newPasswordParameter = request.getParameter("newPassword");
		
		if (newPasswordParameter != null && !newPasswordParameter.isEmpty()) {
			List<String> errorMessages = passValidator.validate(newPasswordParameter);
			if (errorMessages.size() != 0) {
				String errMsg = rb.getString("signup.err.msg.general.error");
				if (errorMessages.contains(PasswordValidator.LENGTH_OR_SPECIAL_CHARACTER_ERROR)) {
					errMsg = rb.getString("signup.err.msg.special.character");
				}
				if (errorMessages.contains(PasswordValidator.MOST_COMMON_PASSWORD)) {
					errMsg = rb.getString("signup.err.msg.common.password");
				}
				request.getSession().setAttribute("errMsg", errMsg);
				response.sendRedirect(baseUrl + "/edit-profile");
				return;
			}
		}
		
		if (newPasswordParameter != null && !newPasswordParameter.isEmpty()) {
			user.setPassword(encryptionService.generatePasswordWithSaltAndHash(newPasswordParameter));
		}
	
		userFacade.updateUser(user);
		response.sendRedirect(baseUrl + "/my-profile");
	}

}

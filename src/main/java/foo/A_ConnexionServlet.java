package foo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

//With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "UserConnexion", description = "Connexion de l'utilisateur", urlPatterns = "/connexion")
public class A_ConnexionServlet extends HttpServlet {

	UserService userService = UserServiceFactory.getUserService();

	String thisUrl, email, nom;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		thisUrl = req.getRequestURI();

		resp.setContentType("text/html");
		if (req.getUserPrincipal() != null) {
			/*
			 * getNicknameUser(); getEmailUser(); req.setAttribute("nom", nom);
			 * this.getServletContext().getRequestDispatcher("/compteView.jsp").forward(req,
			 * resp); req.setAttribute("email", email);
			 * this.getServletContext().getRequestDispatcher("/compteView.jsp").forward(req,
			 * resp);
			 */
			resp.sendRedirect("/view");
		} else {
			resp.sendRedirect(userService.createLoginURL(thisUrl));
		}
	}

	public boolean getConnected() {
		return userService.isUserLoggedIn();
	}

	public String getNicknameUser() {
		return userService.getCurrentUser().getNickname().toString();
	}

	public String getEmailUser() {
		return userService.getCurrentUser().getEmail().toString();
	}

	public void getSignOut() {
		userService.createLogoutURL(thisUrl);
	}

}
//[END users_API_example]

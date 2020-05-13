package foo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

//With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "UserCompteView", description = "Vue de l'utilisateur post", urlPatterns = "/view")
public class A_CompteViewServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		Integer compteur = 0;
		Boolean trouve = false;
		Integer i = 0;
		
		ArrayList<String> FriendUser = new ArrayList<String>();
		
		foo.A_ConnexionServlet connect = new foo.A_ConnexionServlet();
		String user = connect.getNicknameUser();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		
		Query z = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));;
		PreparedQuery pqq = datastore.prepare(z);
		List<Entity> resultat = pqq.asList(FetchOptions.Builder.withDefaults());
		
		for (Entity entityy : resultat) {
			FriendUser = (ArrayList<String>) entityy.getProperty("friends");
		}
				
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		
		
		
		Query q = new Query("Friend");
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		
		ArrayList<String> listesUser = new ArrayList<String>();
		
		for (Entity entity : result) {
			if (compteur <= 20) {
				for  (i = 0; i < FriendUser.size(); i++) { // Faire un while à la place
					if (entity.getProperty("lastName").equals(FriendUser.get(i))) {
						trouve = true;
					} 
				}
				if (trouve == false) {
					compteur = compteur + 1;
					listesUser.add((String) entity.getProperty("lastName"));
				} 
				trouve = false;				
		}
		}
			
			
		req.setAttribute("test2", listesUser);
		this.getServletContext().getRequestDispatcher("/compteView.jsp").forward(req, resp);
		//response.sendRedirect("/view");
	
​
	}


}

//[END users_API_example]
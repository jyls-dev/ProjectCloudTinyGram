package foo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import com.google.appengine.repackaged.com.google.datastore.v1.CompositeFilter;
import com.google.appengine.repackaged.com.google.datastore.v1.Projection;
import com.google.appengine.repackaged.com.google.datastore.v1.PropertyFilter;

@WebServlet(name = "PrefixCleanPost", urlPatterns = { "/prefixcleanpost/*"} )
public class PrefixCleanPost extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String url = "http://localhost:8080/post.jsp";
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
	
		StringBuffer post = request.getRequestURL();
		String res = post.substring(38);
	
		long idPost = Long.parseLong(res);
	
		response.getWriter().print("Identifiant intercepté : " + idPost);
	
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		Entity deletePost = new Entity("Post", idPost);
	
		Key keyPost = deletePost.getKey();
	
		response.getWriter().print("<br><br>Clef créée : " + keyPost);
	
		foo.A_ConnexionServlet connect = new foo.A_ConnexionServlet();
		String user = connect.getNicknameUser();
		
		Query q = new Query("Post").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, user));;
	
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
	
		response.getWriter().print("<br><br>avant boucle for<br>");
		for (Entity entity : result) {
			if (entity.getKey().equals(keyPost)) {
				datastore.delete(entity.getKey());
				response.getWriter().print("<li> deleting" + entity.getKey()+"<br>");
			}
		}
		response.getWriter().print("<br>après boucle for");
		response.sendRedirect(url);
		
	}
}
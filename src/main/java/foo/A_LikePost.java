package foo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("unused")
@WebServlet(name = "A_LikePost", urlPatterns = { "/likepost/*"} )
public class A_LikePost extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String url = "/post.jsp";
	
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
	
		StringBuffer post = request.getRequestURL();
		String res = post.substring(31);
		Boolean trouve = false;
		long idPost = Long.parseLong(res);
	
		Date date = null;
		Long nb_like = Long.parseLong("0");
		String body = "";
		String image = "";
	
		ArrayList<String> ami = new ArrayList<String>();
		String prénom = "";
		String nom = "";
		ArrayList<String> likes = new ArrayList<String>();
		ArrayList<String> likesbis = new ArrayList<String>();
		Long age = Long.parseLong("0");
	
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		Entity likePost = new Entity("Post", idPost);
		Key keyPost = likePost.getKey();
	
		foo.A_ConnexionServlet connect = new foo.A_ConnexionServlet();
		String user = connect.getNicknameUser();
	
		Entity Person = new Entity("Friend",user);
	
		Query z = new Query("Friend").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, user));;
	
		PreparedQuery pz = datastore.prepare(z);
		List<Entity> resultat = pz.asList(FetchOptions.Builder.withDefaults());
		for (Entity entity : resultat) {
			age = (Long) entity.getProperty("age");
			prénom = (String) entity.getProperty("firstName");
			nom = (String) entity.getProperty("lastName");
			ami = (ArrayList<String>) entity.getProperty("friends");
			likes = (ArrayList<String>) entity.getProperty("LikedPost");
			
			if (likes != null) {
			
				for (int i = 0; i < likes.size(); i++) {
				
					if (likes.get(i).equals(res)) {
					
						trouve = true;
						likes.remove(i);
					}
				}
			}
			if (trouve) { // Cas du dislike
		
				Query q = new Query("Post");
			
				PreparedQuery pq = datastore.prepare(q);
				List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
				for (Entity entityy : result) {
				if (entityy.getKey().equals(keyPost)) {
			
				date = (Date) entityy.getProperty("date");
				nb_like = (Long) entityy.getProperty("likec");
				nb_like = nb_like - 1;
				body = (String) entityy.getProperty("body");
				image = (String) entityy.getProperty("url");
				}
				}
			
				Person.setProperty("firstName", prénom);
				Person.setProperty("lastName", nom);
				Person.setProperty("age", age);
				Person.setProperty("friends", ami);
				Person.setProperty("LikedPost", likes);
			
				datastore.put(Person);
			
				likePost.setProperty("body", body);
				likePost.setProperty("date", date);
				likePost.setProperty("likec", nb_like);
				likePost.setProperty("owner", user);
				likePost.setProperty("url", image);
			
				datastore.put(likePost);
			
				response.getWriter().print("<li> Post déjà liké");
			
			}else { // Cas du like
			
				Query q = new Query("Post");
			
				PreparedQuery pq = datastore.prepare(q);
				List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
				
				for (Entity entityy : result) {
					if (entityy.getKey().equals(keyPost)) {
			
						date = (Date) entityy.getProperty("date");
						nb_like = (Long) entityy.getProperty("likec");
						nb_like = nb_like + 1;
						body = (String) entityy.getProperty("body");
						image = (String) entityy.getProperty("url");
					}
				}
			
				if (likes != null) {
					
					likes.add(res);
					Person.setProperty("LikedPost", likes);
				
				} else {
					
					likesbis.add(res);
					Person.setProperty("LikedPost", likesbis);
				}
				
				response.getWriter().print("<br><br>Tableau : " + likes);
				response.getWriter().print("<br><br>Tableau apres remplissage : " + likes);
			
				Person.setProperty("firstName", prénom);
				Person.setProperty("lastName", nom);
				Person.setProperty("age", age);
				Person.setProperty("friends", ami);

				likePost.setProperty("body", body);
				likePost.setProperty("date", date);
				likePost.setProperty("likec", nb_like);
				likePost.setProperty("owner", user);
				likePost.setProperty("url", image);
			
				datastore.put(likePost);
				datastore.put(Person);
				response.getWriter().print("<li> Post a été liké");
			}
		}
		response.sendRedirect(url);
	}	
}

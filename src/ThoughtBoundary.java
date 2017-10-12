import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@WebServlet(urlPatterns={"/ThoughtBoundary"})
public class ThoughtBoundary extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4836792415272384627L;
	
	Configuration cfg = null;

	private String templateDir = "/WEB-INF/templates";
	
	public void init() {
		cfg = new Configuration(Configuration.VERSION_2_3_25);
		cfg.setServletContextForTemplateLoading(getServletContext(), templateDir);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	}
	

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String loginButton = (String) request.getParameter("loginButton");
		if(loginButton != null){
			processLogin(request, response);
			return;
		}
		String newThoughtsButton = (String) request.getParameter("newThoughtsButton");
		if(newThoughtsButton != null){
			addNewThought(request, response);
			return;
		}
		String viewThoughtsButton = (String) request.getParameter("viewThoughtsButton");
		if(viewThoughtsButton != null){
			viewThoughts(request, response);
			return;
		}
		
	}


	private void viewThoughts(HttpServletRequest request, HttpServletResponse response) {
		String fname = (String) request.getParameter("fname");
		Set<String> thoughts = null;
		if(fname != null && !fname.isEmpty()){
			thoughts = ThoughtDB.getInstance().getUserThoughts(fname);
		}
		if(thoughts == null){
			thoughts = new HashSet<>();
		}
		DefaultObjectWrapperBuilder df = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
		SimpleHash root = new SimpleHash(df.build());
		root.put("userName", fname);
		root.put("thoughts", thoughts);
		root.put("size", thoughts.size());
		renderTemplate(response, root, "thoughts.html");
		
	}


	private void renderTemplate(HttpServletResponse response, SimpleHash root, String templateName) {
		try {
			Template template = null;
			template = cfg.getTemplate(templateName );
			response.setContentType("text/html");
			Writer out = response.getWriter();
			template.process(root, out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}


	private void addNewThought(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String newthought = (String) request.getParameter("newthought");
		String fname = (String) request.getParameter("fname");
		if(fname != null && !fname.isEmpty() && newthought != null && !newthought.isEmpty()){
			ThoughtDB.getInstance().addUserThought(fname, newthought);
		}
		DefaultObjectWrapperBuilder df = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
		SimpleHash root = new SimpleHash(df.build());
		root.put("userName", fname);
		renderTemplate(response, root, "mainPage.html");
		
	}


	private void processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fname = (String) request.getParameter("fname");
		String password = (String) request.getParameter("password");
		if(fname == null || fname.isEmpty() || password == null || password.isEmpty()){
			response.sendRedirect("/index.html");
		}
		DefaultObjectWrapperBuilder df = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
		SimpleHash root = new SimpleHash(df.build());
		root.put("userName", fname);
		renderTemplate(response, root, "mainPage.html");
		
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}


}

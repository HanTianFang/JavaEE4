package ServerCon;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("unused")
public class ServletProcessor {


	private Servlet loadServlet(String servletName) throws MalformedURLException {
		String servletURL = "../" + servletName.replace('.', '/');

		File file = new File(servletURL);
		@SuppressWarnings("deprecation")
		URL url = file.toURL();
		@SuppressWarnings("resource")
		URLClassLoader loader = new URLClassLoader(new URL[] { url }, Thread.currentThread().getContextClassLoader());
		Servlet servlet = null;

		try {
			@SuppressWarnings("unchecked")
			Class<Servlet> servletClass = (Class<Servlet>) loader.loadClass(servletName);
			servlet = (Servlet) servletClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return servlet;
	}

	public void processServletRequest(NewServletRequest req, NewServletResponse res) throws Exception {
		String uri = req.getURI();
		String servletName = null;
		if(uri.indexOf(".jsp") != -1){
			servletName = Mapping.getJspMap().get(uri);
		}else if(uri.indexOf(".ico") != 0){
			return;//do nothing
		}
		else{
			servletName = Mapping.getServletMap().get(uri);
		}
		System.out.println("Processing servlet: " + servletName);
		Servlet servlet = loadServlet(servletName);
		callService(servlet, req, res);
	}
	
	private void callService(Servlet servlet, ServletRequest request, ServletResponse response) {
		try {
			servlet.service(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
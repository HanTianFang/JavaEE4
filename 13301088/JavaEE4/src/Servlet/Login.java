package Servlet;import java.io.IOException;import java.io.OutputStream;
import javax.servlet.Servlet;import javax.servlet.ServletConfig;import javax.servlet.ServletException;import javax.servlet.ServletRequest;import javax.servlet.ServletResponse;
import ServerCon.NewServletResponse;
    public class Login implements Servlet {
    	public void destroy() {
    		
    	}
    	public ServletConfig getServletConfig() {
    		return null;
    		}
    	public String getServletInfo() {
    		return null;
    		}
    	public void init(ServletConfig arg0) throws ServletException {
    		
    	}
    	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    		NewServletResponse res = (NewServletResponse) response;
    		OutputStream out = res.getOutputStream1();
    		out.write("<html><head><title>Login</title></head><body>    ".getBytes()); 
    		   if (request.getParameter("username") == null) {
    			   out.write("        <form action='?' method='get'><p>Name: <input type='text' name='username'></p></form>    ".getBytes());
    			   }
    		   else {
    			   out.write("        <p>Hello,  ".getBytes());
    			   out.write(request.getParameter("username").getBytes());
    			   out.write(" !</p>    ".getBytes());
    			   }
    		   out.write("</body></html>".getBytes());   
    		   }
    	}
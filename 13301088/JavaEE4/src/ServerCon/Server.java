package ServerCon;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Server {
	static ServletRequest req = null;
	static ServletResponse res = null;
	
	public static void main(String args[]) throws Exception{
		final int port = 3333; 
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			println("服务器端口： " + serverSocket.getLocalPort());
			Mapping.mapJsp();
			Mapping.mapServlet();
			JspParser.Jsp2Servlet();
			while(true){
				Socket clientSocket = serverSocket.accept();
				println("客户端地址：" + clientSocket.getInetAddress() + ": " + clientSocket.getPort());
				
				service(clientSocket);
				clientSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void service(Socket socket) throws Exception{
		try {
			InputStream socketIn = socket.getInputStream();
			//Thread.sleep(500);
			req = new NewServletRequest(socketIn);
			OutputStream socketOut = socket.getOutputStream();
			res = new NewServletResponse(socketOut);
			
			ServletProcessor sp = new ServletProcessor();
			
			sp.processServletRequest((NewServletRequest)req, (NewServletResponse)res); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void println(String content){
		System.out.println(content);
	}
}

package ServerCon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class JspParser {

	public static void Jsp2Servlet() {
		String root = System.getProperty("user.dir") + "/src";
		File file = new File(root );

		if (file.isDirectory()) {
			String files[] = file.list();

			for (String f : files) {
				if (f.indexOf(".jsp") != -1) {
					final String translateRoot = root + "/Servlet";
					String jspName = f.substring(0, f.indexOf("."));
					File servletFile = new File(translateRoot + "/" + jspName + ".java");
					File JspFile = new File(root + "/" + f);
					try {
						PrintStream ps = new PrintStream(servletFile);
						FileReader fr = new FileReader(JspFile);
						@SuppressWarnings("resource")
						BufferedReader br = new BufferedReader(fr);

						ps.print("package Servlet;" + "import java.io.IOException;" + "import java.io.OutputStream;"
								+ "import javax.servlet.Servlet;" + "import javax.servlet.ServletConfig;"
								+ "import javax.servlet.ServletException;" + "import javax.servlet.ServletRequest;"
								+ "import javax.servlet.ServletResponse;" + "import ServerContainer.MyServletResponse;"
								+ "public class Login implements Servlet {"
								+ "public void destroy() {" + "}"
								+ "public ServletConfig getServletConfig() {"
								+ "return null;" + "}"
								+ "public String getServletInfo() {"
								+ "return null;" + "}"
								+ "public void init(ServletConfig arg0) throws ServletException {"
								+ "}"
								+ "public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {"
								+ "MyServletResponse res = (MyServletResponse) response;"
								+ "OutputStream out = res.getOutputStream1();");
						StringBuffer sb = new StringBuffer();
						String line = null;
						while ((line = br.readLine()) != null)
							sb.append(line);

						String str = sb.toString();
						String HTML_REGEX = "<html>.*?</html>";
						Pattern html_pattern = Pattern.compile(HTML_REGEX);
						Matcher html_matcher = html_pattern.matcher(str);

						if (html_matcher.find()) {
							String htmlStr = html_matcher.group();
							int startHtml = 0;
							int endHtml = htmlStr.length();

							String JSP_REGEX = "<%.*?%>";
							Pattern jsp_pattern = Pattern.compile(JSP_REGEX);
							Matcher jsp_matcher = jsp_pattern.matcher(htmlStr);

							int startJsp = startHtml;
							int endJsp = endHtml;
							while (jsp_matcher.find()) {
								String jspStr = jsp_matcher.group();
								startJsp = jsp_matcher.start();
								String pre = htmlStr.substring(startHtml, startJsp);
								ps.print("out.write(\"" + pre + "\".getBytes());");
								endJsp = jsp_matcher.end();
								String regex = "<%[ ]*=";
								Matcher matcher = Pattern.compile(regex).matcher(jspStr);
								String jsp = null;
								if (matcher.find()) {
									jsp = jspStr.substring(jspStr.indexOf("=") + 1, jspStr.length() - 2);
									ps.print("out.write(" + jsp + ".getBytes());");
								} else {
									jsp = htmlStr.substring(startJsp + 2, endJsp - 2);
									ps.print(jsp);
								}
								startHtml = endJsp;
							}
							String left = htmlStr.substring(startHtml, endHtml);
							ps.print("out.write(\"" + left + "\".getBytes());");
						}

						ps.print("   }" + "}");

						ps.flush();
						ps.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}

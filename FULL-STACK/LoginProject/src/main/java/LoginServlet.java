

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String name =request.getParameter("username");
		String password =request.getParameter("password");
		
		if(name.equals("sujan") && password.equals("sujan")) {
			out.println("Successfully Login");
		}
		else {
			out.println("Invalid login credentials ");
		}

		
	}

}

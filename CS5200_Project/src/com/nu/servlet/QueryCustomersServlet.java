package com.nu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.tools.CustomerOps;

/**
 * Servlet implementation class QueryCustomersServlet
 */
@WebServlet("/QueryCustomersServlet")
public class QueryCustomersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryCustomersServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// result string
		String output = null;
		
		// time to query based on action
		try {
			// read the type parameter from the request
			String type = request.getParameter("type");
			String customer = request.getParameter("customer");
			
			CustomerOps ops = new CustomerOps();
			// build the statement depending on the type
			if(type != null && type.equals("search") && customer != null) {
				customer = "%" + customer + "%";
				output = ops.queryCustomers(customer);
			} else {
				output = ops.showCustomers();
			}
		} catch (Exception e) {
			System.out.println("An exception occurred: " + e.getMessage());
			e.printStackTrace();
			output = "<table><tr><th>An error occurred.  Please consult the logs.</th></tr></table>";
		} finally {
			// add the table back in to the request and send it back to the jsp
			request.setAttribute("output", output);
		}
		
		response.setContentType("text/html");
		response.setStatus(200);
		request.getRequestDispatcher("/customerManagement.jsp").forward(request,response);	
	}

}

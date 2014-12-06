package com.nu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.tools.TicketOps;

/**
 * Servlet implementation class QueryTicketsServlet
 */
@WebServlet("/QueryTicketsServlet")
public class QueryTicketsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryTicketsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String output = null;
		
		try {
			String type = request.getParameter("type");
			String customerName = request.getParameter("customerName");
			String ticketId = request.getParameter("ticketId");
			
			TicketOps to = new TicketOps();
			if(type != null) {
				if(type.equals("showAll")) {
					output = to.showTickets();
				} else if(type.equals("byCustomer") && customerName != null) {
					output = to.queryTicketsByCustomer(customerName);
				} else if(type.equals("byId") && ticketId != null) {
					output = to.queryTicketsById(ticketId);
				}
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
		request.getRequestDispatcher("/ticketManagement.jsp").forward(request,response);
	}

}

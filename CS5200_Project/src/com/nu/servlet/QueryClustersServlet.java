package com.nu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.tools.ClusterOps;

/**
 * Servlet implementation class QueryClustersServlet
 */
@WebServlet("/QueryClustersServlet")
public class QueryClustersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryClustersServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// initialize an output html string
		String output = null;
		
		try {
			String type = request.getParameter("type");
			String clusterName = request.getParameter("clusterName");
			
			ClusterOps co = new ClusterOps();
			if(type != null) {
				if(type.equals("showAll")) {
					output = co.showClusterStats();
				} else if(type.equals("byCluster") && clusterName != null) {
					output = co.showClusterStats(clusterName);
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
		request.getRequestDispatcher("/clusterManagement.jsp").forward(request,response);
	}

}

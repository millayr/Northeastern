package com.nu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.tools.DatabaseOps;

/**
 * Servlet implementation class QueryDatabasesServlet
 */
@WebServlet("/QueryDatabasesServlet")
public class QueryDatabasesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryDatabasesServlet() {
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
			String clusterName = request.getParameter("clusterName");
			String databaseName = request.getParameter("databaseName");
			String shardCount = request.getParameter("shardCount");
			
			DatabaseOps dops = new DatabaseOps();
			if(type != null) {
				if(type.equals("showAll")) {
					output = dops.showDatabases();
				} else if(type.equals("byCluster") && clusterName != null) {
					output = dops.showShards(clusterName);
				} else if(type.equals("updateShardCount") && clusterName != null && shardCount != null) {
					
					if(dops.updateShardCount(clusterName, databaseName, shardCount) == 1) {
						output = "<table><tr><th>SUCCESS! The new shard count for " + clusterName
								+ " is now set to " + shardCount + "</th></tr><table>";
					} else {
						output = "<table><tr><th>No databases were updated.  Please verify your inputs.</th></tr><table>";
					}
				} else if(type.equals("viewIndexes") && clusterName != null && databaseName != null) {
					output = dops.showIndexes(clusterName, databaseName);
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
		request.getRequestDispatcher("/dbManagement.jsp").forward(request,response);
	}

}

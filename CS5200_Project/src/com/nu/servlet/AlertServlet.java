package com.nu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.tools.AlertOps;

/**
 * Servlet implementation class AlertServlet
 */
@WebServlet("/AlertServlet")
public class AlertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlertServlet() {
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
			String alertName = request.getParameter("alertName");
			String threshold = request.getParameter("threshold");
			String applyToAll = request.getParameter("applyToAllClusters");
			
			AlertOps ao = new AlertOps();
			if(type != null) {
				if(type.equals("showAll")) {
					output = ao.showAlerts();
				} else if (type.equals("byCluster") && clusterName != null) {
					output = ao.showByClusterName(clusterName);
				} else if (type.equals("enableAlert") && clusterName != null && alertName != null) {
					output = ao.enableAlertForCluster(clusterName, alertName);
				} else if (type.equals("disableAlert") && clusterName != null && alertName != null) {
					output = ao.disableAlertForCluster(clusterName, alertName);
				} else if (type.equals("newAlert") && alertName != null && threshold != null) {
					output = ao.createNewAlert(alertName, threshold, applyToAll);
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
		request.getRequestDispatcher("/alarmManagement.jsp").forward(request,response);	
	}

}

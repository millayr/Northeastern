package com.nu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.containers.Cluster;
import com.nu.containers.DatabaseNode;
import com.nu.tools.ClusterOps;

/**
 * Servlet implementation class ModifyClusterServlet
 */
@WebServlet("/ModifyClusterServlet")
public class ModifyClusterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyClusterServlet() {
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
			String nodeName = request.getParameter("nodeName");
			String vendor = request.getParameter("vendor");
			
			ClusterOps co = new ClusterOps();
			if(type != null) {
				if(type.equals("addNode") && clusterName != null && nodeName != null && vendor != null) {
					Cluster c = co.getCluster(clusterName);
					DatabaseNode db = new DatabaseNode(vendor, nodeName, c);
					co.addNewNode(db, true, true);
					output = "<table><tr><th>SUCCESS!  A new node was added.</th></tr></table>";
				} else if(type.equals("deleteNode") && clusterName != null && nodeName != null) {
					Cluster c = co.getCluster(clusterName);
					co.deleteNode(c, nodeName);
					output = "<table><tr><th>SUCCESS!  A node was deleted.</th></tr></table>";
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

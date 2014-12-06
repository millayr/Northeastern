package com.nu.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nu.containers.*;
import com.nu.tools.*;

/**
 * Servlet implementation class NewCustomerServlet
 */
@WebServlet("/NewCustomerServlet")
public class NewCustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewCustomerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String output = null;
		
		// time to add the new customer/nodes
		try {
			String customerName = request.getParameter("newCustomer");
			String tamName = request.getParameter("tam");
			String clusterName = request.getParameter("cluster");
			String clusterSize = request.getParameter("size");
			String datacenter = request.getParameter("datacenter");
			String vendor = request.getParameter("vendor");
			
			// get the TAM's id so we can fulfill the foreign key constraint
			Employee tam = null;
			if(tamName != null && !tamName.isEmpty())
				tam = new EmployeeOps().getTAMByName(tamName);
			
			// TODO: verify the inputs are not null
			
			// build the customer
			Customer customer = new Customer();
			customer.name = customerName;
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			customer.since = dateFormat.format(cal.getTime());
			
			// add the tam if we have one
			if(tam != null)
				customer.tam = tam;
			
			// Add the new customer entry
			customer.id = new CustomerOps().addNewCustomer(customer);
			
			// Add a new cluster if all fields were entered
			if(clusterName != null && !clusterName.isEmpty() && clusterSize != null && !clusterSize.isEmpty()
					&& datacenter != null && !datacenter.isEmpty() && vendor != null && !vendor.isEmpty()) {
				
				// build the new cluster
				Cluster cluster = new Cluster();
				cluster.name = clusterName;
				cluster.datacenter = datacenter;
				cluster.ownedBy = customer.id;
				
				// build the nodes
				int numNodes = Integer.parseInt(clusterSize);
				for(int i = 1; i <= numNodes; i++) {
					String nodeName = "db" + i + "." + cluster.name;
					cluster.nodes.add(new DatabaseNode(vendor, nodeName, cluster));
				}
				
				// the cluster needs a lb if there are nodes
				if(numNodes > 0)
					cluster.lb = new LoadBalancerNode(vendor, "lb1." + cluster.name, cluster);
				
				// add the cluster
				new ClusterOps().addNewCluster(cluster);
			}
			
			output = "<table><tr><th>SUCCESS!</th></tr></table>";
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

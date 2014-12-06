package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.nu.containers.Cluster;
import com.nu.containers.DatabaseNode;
import com.nu.containers.Node;

public class AlertOps {
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public String showAlerts() throws Exception {
		// initialize the output string
		String output = "";
		
		String sql = "select * from Alert";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		ResultSet results = query.executeQuery();
		
		// iterate over all the results and build the table
		output = "<table><tr><th>Alert ID</th><th>Alert Name</th><th>Alert Threshold</th></tr>";
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";
			output += "<td>" + results.getString(2) + "</td>";
			output += "<td>" + results.getString(3) + "</td>";
			output += "</tr>";
		}
		output += "</table>";
		query.close();
		// return the html table
		return output;
	}
	
	
	public String showByClusterName(String clusterName) throws Exception {
		//initialize the output string
		String output = "";
		
		String sql = "select a.id, a.name, a.threshold, n.name, c.name from Alert a, Node n, Cluster c, AppliedAlerts aa "
				+ "where aa.alertId = a.id and aa.nodeId = n.id and n.belongsTo = c.id and c.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, clusterName);
		ResultSet results = query.executeQuery();
		
		// iterate over all the results and build the table
		output = "<table><tr><th>Alert ID</th><th>Alert Name</th><th>Alert Threshold</th><th>Node Name</th><th>Cluster Name</th></tr>";
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";
			output += "<td>" + results.getString(2) + "</td>";
			output += "<td>" + results.getString(3) + "</td>";
			output += "<td>" + results.getString(4) + "</td>";
			output += "<td>" + results.getString(5) + "</td>";
			output += "</tr>";
		}
		output += "</table>";
		query.close();
		// return the html table
		return output;
	}
	
	
	public String enableAlertForCluster(String clusterName, String alertId) throws Exception {
		try {
			// there are a handful of db calls for this operation.  Let's use a transaction
			db.getConnection().setAutoCommit(false);
			
			// convert the alert to an int
			int alertIdNum = Integer.parseInt(alertId);
			
			// get all the matching nodes from the cluster
			List<Node> nodes = new ClusterOps().getNodesByCluster(clusterName);
			
			// start applying the alert to each node
			String sql = "insert into AppliedAlerts(nodeId, alertId) values (?, ?)";
			PreparedStatement insert = db.getConnection().prepareStatement(sql);
			insert.setInt(2, alertIdNum);
			
			for(Node n : nodes) {
				insert.setInt(1, n.id);
				insert.executeUpdate();
			}
			
			db.getConnection().commit();
			insert.close();
			return "<table><tr><th>SUCCESS!  The " + nodes.size() + " nodes in " 
				+ clusterName + " had alert Id " + alertId + " applied.</th></tr></table>";
		} catch (Exception e) {
			throw e;
		} finally {
			// revert the auto commit flag
			db.getConnection().setAutoCommit(true);
		}
	}
	
	
	public String disableAlertForCluster(String clusterName, String alertId) throws Exception {
		try {
			// there are a handful of db calls for this operation.  Let's use a transaction
			db.getConnection().setAutoCommit(false);
			
			// convert the alert to an int
			int alertIdNum = Integer.parseInt(alertId);
			
			// get all the matching nodes from the cluster
			List<Node> nodes = new ClusterOps().getNodesByCluster(clusterName);
			
			// start applying the alert to each node
			String sql = "delete from AppliedAlerts where nodeId = ? and alertId = ?";
			PreparedStatement delete = db.getConnection().prepareStatement(sql);
			delete.setInt(2, alertIdNum);
			
			for(Node n : nodes) {
				delete.setInt(1, n.id);
				delete.executeUpdate();
			}
			
			db.getConnection().commit();
			delete.close();
			return "<table><tr><th>SUCCESS!  The " + nodes.size() + " nodes in " 
				+ clusterName + " had alert Id " + alertId + " removed.</th></tr></table>";
		} catch (Exception e) {
			throw e;
		} finally {
			// revert the auto commit flag
			db.getConnection().setAutoCommit(true);
		}
	}
	
	
	public String createNewAlert(String alertName, String threshold, String applyToAllClusters) throws Exception {
		try {
			// there are a handful of db calls for this operation.  Let's use a transaction
			db.getConnection().setAutoCommit(false);
			
			// convert the threshold to an int
			int thresholdNum = Integer.parseInt(threshold);
			
			// first create the new alert entry
			String sql = "insert into Alert(name, threshold) values (?, ?)";
			PreparedStatement insert = db.getConnection().prepareStatement(sql);
			insert.setString(1, alertName);
			insert.setInt(2, thresholdNum);
			insert.executeUpdate();
			insert.close();
			
			// check whether we want to applied this to all of the clusters in the database
			if(applyToAllClusters != null && applyToAllClusters.equalsIgnoreCase("Yes")) {	
				// we need the last id created in the alert table
				PreparedStatement query = db.getConnection().prepareStatement("select last_insert_id() from Alert");
				ResultSet results = query.executeQuery();
				results.next();
				int alertId = results.getInt(1);
				
				// get a list of all the clusters in the database
				List<Cluster> clusters = new ClusterOps().getAllClusters(false);
				
				// create the prepared statement
				sql = "insert into AppliedAlerts(nodeId, alertId) values (?, ?)";
				PreparedStatement applyAlert = db.getConnection().prepareStatement(sql);
				applyAlert.setInt(2, alertId);
				
				// iterate over all clusters and all nodes and apply the alert
				for(Cluster c : clusters) {
					if(c.lb != null) {
						applyAlert.setInt(1, c.lb.id);
						applyAlert.executeUpdate();
					}
					
					for(DatabaseNode db : c.nodes) {
						applyAlert.setInt(1, db.id);
						applyAlert.executeUpdate();
					}
				}
				query.close();
			}
			
			// commit the updates
			db.getConnection().commit();
			
			// return a success string
			return "<table><tr><th>SUCCESS!  Alert was created!</th></tr></table>";
		} catch (Exception e) {
			throw e;
		} finally {
			// revert the auto commit flag
			db.getConnection().setAutoCommit(true);			
		}
	}
}

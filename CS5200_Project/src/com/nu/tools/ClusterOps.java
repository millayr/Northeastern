package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.nu.containers.Cluster;
import com.nu.containers.DatabaseNode;
import com.nu.containers.LoadBalancerNode;
import com.nu.containers.Node;

public class ClusterOps {
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public void addNewCluster(Cluster c) throws Exception{
		try {
			// there are a handful of db calls for this operation.  Let's use a transaction
			db.getConnection().setAutoCommit(false);
			
			String sql = "insert into Cluster(name, datacenter, clusterSize, ownedBy) "
					+ "values (?, ?, ?, ?)";
			
			// build and insert the new cluster
			PreparedStatement newCluster = db.getConnection().prepareStatement(sql);
			newCluster.setString(1, c.name);
			newCluster.setString(2, c.datacenter);
			newCluster.setInt(3, c.nodes.size());
			newCluster.setInt(4, c.ownedBy);
			newCluster.executeUpdate();
			
			// get the auto_incremented Id
			PreparedStatement query = db.getConnection().prepareStatement("select last_insert_id() from Cluster");
			ResultSet results = query.executeQuery();
			results.next();
			c.id = results.getInt(1);
			
			// first add the load balancer
			c.lb.id = addNewNode(c.lb, false, false);
			
			// now, for each node, we should add new nodes
			for(DatabaseNode node : c.nodes) {
				node.lb = c.lb;
				node.cluster = c;
				addNewNode(node, false, false);
			}
			
			// commit the updates
			db.getConnection().commit();
			newCluster.close();
			results.close();
		} catch (Exception e) {
			throw e;
		} finally {
			// revert the auto commit flag
			db.getConnection().setAutoCommit(true);
		}
	}
	
	public int addNewNode(Node n, boolean useTransaction, boolean updateClusterSize) throws Exception {
		try {
			if(useTransaction) {
				// use a transaction to ensure we get the id
				db.getConnection().setAutoCommit(false);
			}
			
			String sql = "insert into Node(name, brand, disk, memory, ip, belongsTo) values "
					+ "(?, ?, ?, ?, ?, ?)";

			PreparedStatement insert = db.getConnection().prepareStatement(sql);
			insert.setString(1, n.name);
			insert.setString(2, n.brand);
			insert.setInt(3, n.diskSize);
			insert.setInt(4, n.memorySize);
			insert.setString(5, n.ip);
			insert.setInt(6, n.cluster.id);
			insert.executeUpdate();
			insert.close();
			
			// get the auto_incremented Id
			PreparedStatement query = db.getConnection().prepareStatement("select last_insert_id() from Node");
			ResultSet results = query.executeQuery();
			db.getConnection().commit();
			results.next();
			n.id = results.getInt(1);
			
			// Depends on the type of node
			if(n instanceof DatabaseNode) {
				addNewDatabaseNode((DatabaseNode)n);
			} else {
				addNewLoadBalancerNode((LoadBalancerNode)n);
			}
			
			// update the cluster size if necessary
			if(updateClusterSize) 
				updateClusterSize(n.cluster, 1);
			
			if(useTransaction) 
				db.getConnection().commit();
			query.close();
			
			return n.id;
		} catch (Exception e) {
			throw e;
		} finally {
			if(useTransaction)
				db.getConnection().setAutoCommit(true);
		}
	}
	
	
	private void addNewDatabaseNode(DatabaseNode n) throws Exception {
		String sql = "insert into DatabaseNode(id, softwareVersion, loadBalancer) values (? , ?, ?)";
		
		PreparedStatement insert = db.getConnection().prepareStatement(sql);
		insert.setInt(1, n.id);
		insert.setInt(2, n.softwareVersion);
		insert.setInt(3, n.lb.id);
		insert.executeUpdate();
		insert.close();
	}
	
	
	private void addNewLoadBalancerNode(LoadBalancerNode n) throws Exception {
		String sql = "insert into LoadBalancerNode(id, keepAlive) values (?, ?)";
		
		PreparedStatement insert = db.getConnection().prepareStatement(sql);
		insert.setInt(1, n.id);
		insert.setInt(2, n.keepalive);
		insert.executeUpdate();
		insert.close();
	}
	
	
	public void updateClusterSize(Cluster c, int amount) throws Exception {
		String sql = "update cluster set clusterSize = ? where id = ?";
		
		PreparedStatement update = db.getConnection().prepareStatement(sql);
		update.setInt(1, c.nodes.size() + amount);
		update.setInt(2, c.id);
		update.executeUpdate();
		update.close();
	}
	
	
	// returns all the nodes for a cluster (both load balancers and database nodes)
	public List<Node> getNodesByCluster(String clusterName) throws Exception {
		// initialize the list to return
		List<Node> nodes = new ArrayList<Node>();
		
		String sql = "select n.id, n.name, n.brand, n.disk, n.memory, n.ip, n.belongsTo "
				+ "from Node n, Cluster c where n.belongsTo = c.id and c.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, clusterName);
		ResultSet results = query.executeQuery();
		
		// iterate over the results and build node objects
		while(results.next()) {
			Node n = new Node(results.getInt(1), results.getString(2), results.getString(3),
					results.getInt(4), results.getInt(5), results.getString(6), results.getInt(7));
			nodes.add(n);
		}
		query.close();
		
		// return the list of nodes
		return nodes;
	}
	
	
	public List<Cluster> getAllClusters(boolean useTransaction) throws Exception {
		// initialize the list to return
		List<Cluster> clusters = new ArrayList<Cluster>();
		
		try {
			if(useTransaction)
				db.getConnection().setAutoCommit(false);
			
			// get all of the clusters
			String sql = "select * from Cluster";
			PreparedStatement query = db.getConnection().prepareStatement(sql);
			ResultSet results = query.executeQuery();
			
			// iterate over all results and build the full picture
			while(results.next()) {
				Cluster c = new Cluster();
				c.id = results.getInt(1);
				c.name = results.getString(2);
				c.datacenter = results.getString(3);
				c.ownedBy = results.getInt(5);
				
				// get the nodes
				List<Node> allNodes = getNodesByCluster(c.name);
				
				// separate the nodes based on load balancer and database nodes
				for(Node n : allNodes) {
					if(n.name.startsWith("lb"))
						c.lb = new LoadBalancerNode(n.id, n.name, n.brand, n.diskSize, n.memorySize, n.ip, c);
					else
						c.nodes.add(new DatabaseNode(n.id, n.name, n.brand, n.diskSize, n.memorySize, n.ip, c));
				}
				
				clusters.add(c);
			}
			
			if(useTransaction)
				db.getConnection().commit();
			query.close();
			
			return clusters;
		} catch (Exception e) {
			throw e;
		} finally {
			if(useTransaction) 
				db.getConnection().setAutoCommit(true);
		}
	}
	
	
	public String showClusterStats() throws Exception {
		// initialize the html table
		String output = "<table><tr><th>Cluster Id</th><th>Cluster Name</th><th>Data Center</th>"
				+ "<th>Node Name</th><th>Vendor</th><th>Disk Size (GB)</th><th>Memory (GB)</th>"
				+ "<th>IP Address</th><th>Owner</th></tr>";
		
		// build the query
		String sql = "select cl.id, cl.name, cl.datacenter, n.name, n.brand, n.disk, n.memory, n.ip, c.name "
				+ "from Cluster cl, Customer c, Node n where cl.ownedBy = c.id and n.belongsTo = cl.id "
				+ "order by cl.name";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		ResultSet results = query.executeQuery();
		
		// iterate over the results
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";  // id
			output += "<td>" + results.getString(2) + "</td>";  // cluster name
			output += "<td>" + results.getString(3) + "</td>";  // data center
			output += "<td>" + results.getString(4) + "</td>";  // node name
			output += "<td>" + results.getString(5) + "</td>";  // node vendor
			output += "<td>" + results.getInt(6) + "</td>";  // disk size
			output += "<td>" + results.getInt(7) + "</td>";  // memory
			output += "<td>" + results.getString(8) + "</td>";  // ip address
			output += "<td>" + results.getString(9) + "</td>";  // customer name
			output += "</tr>";
		}
		query.close();
		
		output += "</table>";
		return output;
	}
	
	
	public String showClusterStats(String clusterName) throws Exception {
		// initialize the html table
		String output = "<table><tr><th>Cluster Id</th><th>Cluster Name</th><th>Data Center</th>"
				+ "<th>Node Name</th><th>Vendor</th><th>Disk Size (GB)</th><th>Memory (GB)</th>"
				+ "<th>IP Address</th><th>Owner</th></tr>";
		
		// build the query
		String sql = "select cl.id, cl.name, cl.datacenter, n.name, n.brand, n.disk, n.memory, n.ip, c.name "
				+ "from Cluster cl, Customer c, Node n where cl.ownedBy = c.id and n.belongsTo = cl.id "
				+ "and cl.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, clusterName);
		ResultSet results = query.executeQuery();
		
		// iterate over the results
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";  // id
			output += "<td>" + results.getString(2) + "</td>";  // cluster name
			output += "<td>" + results.getString(3) + "</td>";  // data center
			output += "<td>" + results.getString(4) + "</td>";  // node name
			output += "<td>" + results.getString(5) + "</td>";  // node vendor
			output += "<td>" + results.getInt(6) + "</td>";  // disk size
			output += "<td>" + results.getInt(7) + "</td>";  // memory
			output += "<td>" + results.getString(8) + "</td>";  // ip address
			output += "<td>" + results.getString(9) + "</td>";  // customer name
			output += "</tr>";
		}
		query.close();
		
		output += "</table>";
		return output;
	}
	
	
	public Cluster getCluster(String clusterName) throws Exception {
		// initialize the cluster to return
		Cluster cluster = new Cluster();
		
		try {
			db.getConnection().setAutoCommit(false);
			
			// get all of the clusters
			String sql = "select * from Cluster where name = ?";
			PreparedStatement query = db.getConnection().prepareStatement(sql);
			query.setString(1, clusterName);
			ResultSet results = query.executeQuery();
			
			if(results.next()) {
				cluster.id = results.getInt(1);
				cluster.name = results.getString(2);
				cluster.datacenter = results.getString(3);
				cluster.ownedBy = results.getInt(5);
				
				// get the nodes
				List<Node> allNodes = getNodesByCluster(cluster.name);
				
				// separate the nodes based on load balancer and database nodes
				for(Node n : allNodes) {
					if(n.name.startsWith("lb"))
						cluster.lb = new LoadBalancerNode(n.id, n.name, n.brand, n.diskSize, n.memorySize, n.ip, cluster);
					else
						cluster.nodes.add(new DatabaseNode(n.id, n.name, n.brand, n.diskSize, n.memorySize, n.ip, cluster));
				}
			}

			db.getConnection().commit();
			query.close();
			
			return cluster;
		} catch (Exception e) {
			throw e;
		} finally {
			db.getConnection().setAutoCommit(true);
		}
	}
	
	
	public void deleteNode(Cluster c, String nodeName) throws Exception {
		try {
			// use a transaction for the following two operations
			db.getConnection().setAutoCommit(false);
			
			// build the sql string to delete the node
			String sql = "delete n from Node n "
					+ "join cluster c on n.belongsTo = c.id "
					+ "where c.name = ? and n.name = ?";
			
			// execute the prepared statement
			PreparedStatement delete = db.getConnection().prepareStatement(sql);
			delete.setString(1, c.name);
			delete.setString(2, nodeName);
			delete.executeUpdate();
			
			// update the cluster size
			updateClusterSize(c, -1);
			
			db.getConnection().commit();
			delete.close();
		} catch (Exception e) { 
			throw e;
		} finally {
			db.getConnection().setAutoCommit(true);
		}

	}
}

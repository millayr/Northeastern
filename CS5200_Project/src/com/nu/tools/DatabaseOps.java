package com.nu.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseOps {
	// grab the db connection instance
	private DbConnection db = DbConnection.getInstance();
	
	public String showShards(String clusterName) throws Exception {
		String output = "";
		
		// build the query.  This one gets complex and spans several tables
		String sql = "select s.id, s.mapping, dl.name, n.name, c.name "
				+ "from databaselist dl, shard s, node n, sharddistribution sd, databasenode d, cluster c "
				+ "where s.databaseId = dl.id and s.id = sd.shardId and sd.databaseNodeId = d.id and "
				+ "d.id = n.id and n.belongsTo = c.id and c.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, clusterName);
		ResultSet results = query.executeQuery();
		
		// iterate over all the results
		output = "<table><tr><th>Shard Id</th><th>Shard Mapping</th><th>Database Name</th>"
				+ "<th>Node Name</th><th>Cluster Name</th></tr>";
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";  // shard Id
			output += "<td>" + results.getString(2) + "</td>";  // mapping
			output += "<td>" + results.getString(3) + "</td>";  // database name
			output += "<td>" + results.getString(4) + "</td>";  // node name
			output += "<td>" + results.getString(5) + "</td>";  // cluster name
			output += "</tr>";
		}
		output += "</table>";
		query.close();
		// return the html string
		return output;
	}
	
	
	public String showDatabases() throws Exception {
		String output = "";
		
		// build the query.  This one gets complex and spans several tables
		String sql = "select distinct dl.id, dl.name, dl.diskSize, dl.defaultShardCount, c.name "
				+ "from databaselist dl, sharddistribution sd, shard s, databaseNode dn, node n, cluster c "
				+ "where s.databaseId = dl.id and sd.shardid = s.id and sd.databaseNodeId = dn.id "
				+ "and dn.id = n.id and n.belongsTo = c.id";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		ResultSet results = query.executeQuery();
		
		// iterate over all the results
		output = "<table><tr><th>Database Id</th><th>Database Name</th>"
				+ "<th>Size on Disk (GB)</th><th>Default Shard Count</th><th>Cluster Name</th></tr>";
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";  // database Id
			output += "<td>" + results.getString(2) + "</td>";  // database name
			output += "<td>" + results.getInt(3) + "</td>";  // database disk size
			output += "<td>" + results.getInt(4) + "</td>";  // shard count
			output += "<td>" + results.getString(5) + "</td>";  // cluster name
			output += "</tr>";
		}
		output += "</table>";
		query.close();
		// return the html string
		return output;
	}
	
	
	public int updateShardCount(String clusterName, String databaseName, String shardCount) throws Exception {
		// convert the shard count to an int
		int shardCountNum = Integer.parseInt(shardCount);
		
		// build the update sql statement
		String sql = "update databaselist dl "
				+ "join shard s on s.databaseId = dl.id "
				+ "join sharddistribution sd on sd.shardId = s.id "
				+ "join databasenode dn on sd.databaseNodeId = dn.id "
				+ "join node n on dn.id = n.id "
				+ "join cluster c on n.belongsTo = c.id "
				+ "set defaultShardCount = ? "
				+ "where dl.name = ? and c.name = ?";
		PreparedStatement update = db.getConnection().prepareStatement(sql);
		update.setInt(1, shardCountNum);
		update.setString(2, databaseName);
		update.setString(3, clusterName);
		
		// run the update
		int numUpdates = update.executeUpdate();
		update.close();
		return numUpdates;
	}
	
	
	public String showIndexes(String clusterName, String databaseName) throws Exception {
		// initialize the output string
		String output = "";
		
		// build the query
		String sql = "select distinct di.id, di.name, it.name, di.createdOn, dl.name, c.name "
				+ "from databaseindex di, indextype it, databaselist dl, shard s, " 
				+ "sharddistribution sd, databasenode dn, node n, cluster c "
				+ "where di.databaseId = dl.id "
				+ "and di.indexType = it.id "
				+ "and dl.id = s.databaseId "
				+ "and s.id = sd.shardId "
				+ "and sd.databaseNodeId = dn.id "
				+ "and dn.id = n.id "
				+ "and n.belongsTo = c.id "
				+ "and dl.name = ? and c.name = ?";
		PreparedStatement query = db.getConnection().prepareStatement(sql);
		query.setString(1, databaseName);
		query.setString(2, clusterName);
		ResultSet results = query.executeQuery();
		
		// iterate over the results
		output = "<table><tr><th>Index Id</th><th>Index Name</th><th>Index Type</th><th>Date Created</th>"
				+ "<th>Database Name</th><th>Cluster Name</th></tr>";
		while(results.next()) {
			output += "<tr>";
			output += "<td>" + results.getInt(1) + "</td>";  // index id
			output += "<td>" + results.getString(2) + "</td>";  // index name
			output += "<td>" + results.getString(3) + "</td>";  // index type
			output += "<td>" + results.getString(4) + "</td>";  // creation date
			output += "<td>" + results.getString(5) + "</td>";  // database name
			output += "<td>" + results.getString(6) + "</td>";  // cluster name
			output += "</tr>";
		}
		output += "</table>";
		query.close();
		// return the html table
		return output;
	}
}

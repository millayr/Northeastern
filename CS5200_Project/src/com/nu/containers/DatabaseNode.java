package com.nu.containers;

public class DatabaseNode extends Node{
	public int softwareVersion;
	public LoadBalancerNode lb;
	
	// Set all members in the constructor
	public DatabaseNode(int id, String name, String brand, int diskSize, int memorySize, String ip, Cluster cluster) {
		super(id, name, brand, diskSize, memorySize, ip, cluster);
		softwareVersion = 2162;
		lb = cluster.lb;
	}
	
	public DatabaseNode(String vendor, String nodeName, Cluster c) {
		super(vendor, nodeName, c);
		softwareVersion = 2162;
		lb = cluster.lb;
	}
}

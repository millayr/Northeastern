package com.nu.containers;

public class LoadBalancerNode extends Node {
	public int keepalive;
	
	// Set all members in the constructor
	public LoadBalancerNode(int id, String name, String brand, int diskSize, int memorySize, String ip, Cluster cluster) {
		super(id, name, brand, diskSize, memorySize, ip, cluster);
		keepalive = 60;
	}
	
	public LoadBalancerNode(String vendor, String nodeName, Cluster c) {
		super(vendor, nodeName, c);
		keepalive = 60;
	}
}

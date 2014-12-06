package com.nu.containers;

import java.util.Random;

public class Node {
	public int id;
	public String name;
	public String brand;
	public int diskSize;
	public int memorySize;
	public String ip;
	public Cluster cluster;
	
	// Empty constructor
	public Node() {}
	
	// Set all members in the constructor
	public Node(int id, String name, String brand, int diskSize, int memorySize, String ip, Cluster cluster) {
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.diskSize = diskSize;
		this.memorySize = memorySize;
		this.ip = ip;
		this.cluster = cluster;
	}
	
	// Set all members but create a new cluster object with just the cluster id
	public Node(int id, String name, String brand, int diskSize, int memorySize, String ip, int clusterId) {
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.diskSize = diskSize;
		this.memorySize = memorySize;
		this.ip = ip;
		this.cluster = new Cluster();
		this.cluster.id = clusterId;
	}
	
	// initialize a fresh node.  used when creating new entries in the database
	public Node(String vendor, String nodeName, Cluster c) {
		if(vendor.equalsIgnoreCase("dell")) {
			brand = vendor.toUpperCase();
			diskSize = 1500;
			memorySize = 64;
		} else if(vendor.equalsIgnoreCase("ibm")) {
			brand = vendor.toUpperCase();
			diskSize = 2200;
			memorySize = 64;
		} else if(vendor.equalsIgnoreCase("cisco")) {
			brand = vendor.toUpperCase();
			diskSize = 2200;
			memorySize = 64;
		} else {
			brand = "Amazon Web Services VM";
			diskSize = 750;
			memorySize = 24;
		}
		
		cluster = c;
		name = nodeName + ".dbaas.com";
		
		Random rand = new Random();
		ip = Integer.toString(rand.nextInt(999)) + "." + Integer.toString(rand.nextInt(999))
				+ "." + Integer.toString(rand.nextInt(999)) + "." + Integer.toString(rand.nextInt(999));
	}
}

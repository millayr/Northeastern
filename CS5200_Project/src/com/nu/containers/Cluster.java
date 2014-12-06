package com.nu.containers;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	public int id;
	public String name;
	public String datacenter;
	public int ownedBy;
	public List<DatabaseNode> nodes = new ArrayList<DatabaseNode>();
	public LoadBalancerNode lb = null;
}

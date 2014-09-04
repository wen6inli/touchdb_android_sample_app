package com.example.touchdbtest;


import java.util.List;

import org.ektorp.support.CouchDbRepositorySupport;

public class VehicleRepository extends CouchDbRepositorySupport<Vehicle> {
	
	protected static VehicleRepository instance;

	public static synchronized VehicleRepository getInstance() {
		if (instance == null) {
			instance = new VehicleRepository();
		}
		return instance;
	}
	
	public VehicleRepository() {
		super(Vehicle.class, CouchbaseUtils.getConnectorVehicle());
	}

	public List<Vehicle> findByPL(String PL) {
	  return this.queryView("view", PL);
	}
	
}

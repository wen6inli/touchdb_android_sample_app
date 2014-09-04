package com.example.touchdbtest;


import java.io.Serializable;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.support.CouchDbDocument;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle extends CouchDbDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953666802706195215L;

	// "_id": ObjectId("51f978fcaa129c536600003c"),
	// "_type": "CommercialVehicle",
	// "auto_filter": true,
	// "created_at": ISODate("2013-07-31T20:52:12.350Z"),
	// "delivery_agenda_ids": [ObjectId("51f979f9aa129c53b1000001")],
	// "license_plate_number": "xyz123",
	// "license_plate_state": "NY",
	// "updated_at": ISODate("2013-07-31T20:52:12.363Z"),
	// "vendor_ids": [ObjectId("51f978fbaa129c5366000016")],
	public enum TAP {NotTap, TapNotInSchedule, TapinSchedule};
	private String model;
	private String color;
	private String type;
	private String tap_status;

	private ArrayList<String> vendor_ids;
	private ArrayList<String> tenant_ids;

	private String img;
	private String make;
	private String country;
	private String year;
	private TAP tap;
	private String status;
	private String license_plate_number;
	private String miss_read_lpn;
	private String license_plate_state;
	private String controlPoint;
	private String lane;
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public TAP getTap() {
		return tap;
	}

	public void setTap(TAP tap) {
		this.tap = tap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLicense_plate_number() {
		return license_plate_number;
	}

	public void setLicense_plate_number(String license_plate_number) {
		this.license_plate_number = license_plate_number;
	}

	public String getLicense_plate_state() {
		return license_plate_state;
	}

	public void setLicense_plate_state(String license_plate_state) {
		this.license_plate_state = license_plate_state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

 

	public String getLane() {
		return lane;
	}

	public void setLane(String lane) {
		this.lane = lane;
	}

	public String getControlPoint() {
		return controlPoint;
	}

	public void setControlPoint(String controlPoint) {
		this.controlPoint = controlPoint;
	}

	public ArrayList<String> getVendor_ids() {
		return vendor_ids;
	}

	public void setVendor_ids(ArrayList<String> vendor_ids) {
		this.vendor_ids = vendor_ids;
	}

	public ArrayList<String> getTenant_ids() {
		return tenant_ids;
	}

	public void setTenant_ids(ArrayList<String> tenant_ids) {
		this.tenant_ids = tenant_ids;
	}

	public String getMiss_read_lpn() {
		return miss_read_lpn;
	}

	public void setMiss_read_lpn(String miss_read_lpn) {
		this.miss_read_lpn = miss_read_lpn;
	}

	public String getTap_status() {
		return tap_status;
	}

	public void setTap_status(String tap_status) {
		this.tap_status = tap_status;
	}
 
 


}

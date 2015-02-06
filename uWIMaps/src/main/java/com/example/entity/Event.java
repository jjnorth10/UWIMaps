package com.example.entity;

public class Event {
	private long id;
	private String name;
	private String description;
	private long locationId;
	public Event() {
		// TODO Auto-generated constructor stub
	}
	public Event(long id, String name, String description, long locationId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.locationId = locationId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	
	

}

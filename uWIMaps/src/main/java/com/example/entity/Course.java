package com.example.entity;

public class Course {
	private long id;
	private String subject;
	private int code;
	private String title;
	private String lecturer;
	private long locationId;
	public Course() {
		// TODO Auto-generated constructor stub
	}
	public Course(long id, String subject, int code, String title,
			String lecturer, long locationId) {
		super();
		this.id = id;
		this.subject = subject;
		this.code = code;
		this.title = title;
		this.lecturer = lecturer;
		this.locationId = locationId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLecturer() {
		return lecturer;
	}
	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	
	

}

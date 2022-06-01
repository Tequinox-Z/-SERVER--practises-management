package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NewEnrollmentDTO implements Serializable {

	private static final long serialVersionUID = -9147305346693669322L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date date;									// Fecha de matriculación
	private String dniStudent;
	
	public NewEnrollmentDTO() {
		super();
	}

	public NewEnrollmentDTO(Date date, String dniStudent) {
		super();
		this.date = date;
		this.dniStudent = dniStudent;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDniStudent() {
		return dniStudent;
	}

	public void setDniStudent(String dniStudent) {
		this.dniStudent = dniStudent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, dniStudent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewEnrollmentDTO other = (NewEnrollmentDTO) obj;
		return Objects.equals(date, other.date) && Objects.equals(dniStudent, other.dniStudent);
	}

	@Override
	public String toString() {
		return "NewEnrollmentDTO [date=" + date + ", dniStudent=" + dniStudent + "]";
	}
	
}

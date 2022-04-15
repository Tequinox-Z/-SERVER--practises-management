package com.tx.practisesmanagement.dto;


import java.io.Serializable;
import java.util.Objects;
/**
 * DTO de informe
 * @author Salva
 */
public class BriefingDTO implements Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer students;
	private Integer teachers;
	private Integer administrators;
	
	
	public BriefingDTO(Integer students, Integer teachers, Integer administrators) {
		super();
		this.students = students;
		this.teachers = teachers;
		this.administrators = administrators;
	}


	@Override
	public int hashCode() {
		return Objects.hash(administrators, students, teachers);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BriefingDTO other = (BriefingDTO) obj;
		return Objects.equals(administrators, other.administrators) && Objects.equals(students, other.students)
				&& Objects.equals(teachers, other.teachers);
	}


	@Override
	public String toString() {
		return "BriefingDTO [students=" + students + ", teachers=" + teachers + ", administrators=" + administrators
				+ "]";
	}


	public Integer getStudents() {
		return students;
	}


	public void setStudents(Integer students) {
		this.students = students;
	}


	public Integer getTeachers() {
		return teachers;
	}


	public void setTeachers(Integer teachers) {
		this.teachers = teachers;
	}


	public Integer getAdministrators() {
		return administrators;
	}


	public void setAdministrators(Integer administrators) {
		this.administrators = administrators;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}

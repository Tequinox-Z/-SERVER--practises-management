package com.tx.practisesmanagement.dto;


import java.io.Serializable;
import java.util.Objects;
/**
 * DTO de informe básico sobre el centro
 * @author Salva
 */
public class BriefingDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer students;										// Estudiantes
	private Integer teachers;										// Profesores
	private Integer administrators;									// Administradores
	private Integer business;										// Empresas
	
	/**
	 * Constructor básico
	 */
	public BriefingDTO() {
		super();
	}

	/**
	 * Constructor con datos
	 * @param students: Estudiantes
	 * @param teachers: Profesores
	 * @param administrators: Administradores
	 * @param business: Empresas
	 */
	public BriefingDTO(Integer students, Integer teachers, Integer administrators, Integer business) {
		super();
		this.students = students;
		this.teachers = teachers;
		this.administrators = administrators;
		this.business = business;
	}

	@Override
	public String toString() {
		return "BriefingDTO [students=" + students + ", teachers=" + teachers + ", administrators=" + administrators
				+ ", business=" + business + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(administrators, business, students, teachers);
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
		return Objects.equals(administrators, other.administrators) && Objects.equals(business, other.business)
				&& Objects.equals(students, other.students) && Objects.equals(teachers, other.teachers);
	}

	/**
	 * Obtiene la empresa
	 * @return
	 */
	public Integer getBusiness() {
		return business;
	}




	/**
	 * Establece la empresa
	 * @param business
	 */
	public void setBusiness(Integer business) {
		this.business = business;
	}

	/**
	 * Obtiene los estudiantes
	 * @return
	 */
	public Integer getStudents() {
		return students;
	}


	/**
	 * Establece los estudiantes
	 * @param students
	 */
	public void setStudents(Integer students) {
		this.students = students;
	}


	/**
	 * Obtiene los profesores
	 * @return
	 */
	public Integer getTeachers() {
		return teachers;
	}

	/**
	 * Establece los profesores
	 * @param teachers
	 */
	public void setTeachers(Integer teachers) {
		this.teachers = teachers;
	}

	
	/**
	 * Obtiene los administradores
	 * @return
	 */
	public Integer getAdministrators() {
		return administrators;
	}


	/**
	 * Establece los administradores
	 * @param administrators
	 */
	public void setAdministrators(Integer administrators) {
		this.administrators = administrators;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}

package com.tx.practisesmanagement.model;



import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Clase de práctica
 * @author Salva
 */
@Entity
public class Practise {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;									// Identificador de la práctica
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date start;									// Fecha de inicio
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date finish;								// Fecha de fin
		
	@OneToOne(mappedBy = "practise", fetch = FetchType.EAGER)
	@JsonIgnore
	private Enrollment enrollment;						// Matrícula
	
	@ManyToOne
	@JsonIgnore
	private Teacher teacher;
	
	@ManyToOne
	@JsonIgnore
	private LaborTutor laborTutor;
	
	@ManyToOne
	@JsonIgnore
	private Business business;							// Empresa
	
	/**
	 * Constructor sin parámetros
	 */
	public Practise() {
		super();
	}

	/**
	 * Constructor con todos los parámetros
	 * @param id: Identificador
	 * @param start: Fecha inicio
	 * @param finish: Fecha fin
	 * @param enrollment: Matricula
	 * @param business: Empresa
	 */
	public Practise(Integer id, Date start, Date finish, Enrollment enrollment, Business business) {
		super();
		this.id = id;
		this.start = start;
		this.finish = finish;
		this.enrollment = enrollment;
		this.business = business;
	}
	
	
	public Practise(Date start, Date finish) {
		super();
		this.start = start;
		this.finish = finish;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	/**
	 * Constructor con fechas, matrícula y empresa
	 * @param start: FEcha inicio
	 * @param finish: Fecha fin
	 * @param enrollment: Matrícula
	 * @param business: Empresa
	 */
	public Practise(Date start, Date finish, Enrollment enrollment, Business business) {
		super();
		this.start = start;
		this.finish = finish;
		this.enrollment = enrollment;
		this.business = business;
	}

	/**
	 * Obtiene la matrícula
	 * @return Matrícula
	 */
	public Enrollment getEnrollment() {
		return enrollment;
	}

	/**
	 * Establece la matrícula
	 * @param enrollment: Nueva matrícula
	 */
	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	/**
	 * Obtiene la fecha de inicio
	 * @return Fecha de inicio
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * Establece la fecha de inico
	 * @param start: Nueva fecha de inicio
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * Obtiene la fecha de finalización 
	 * @return: Fecha de finalización
	 */
	public Date getFinish() {
		return finish;
	}

	/**
	 * Establece la fecha de finalización
	 * @param finish: Fecha de finalización
	 */
	public void setFinish(Date finish) {
		this.finish = finish;
	}

	/**
	 * Obtiene el identificador
	 * @return: Identificador
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el idenfitificador
	 * @param id: Nuevo identificador
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene la empresa
	 * @return: Empresa
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * Establece la empresa
	 * @param business: Nueva empresa
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}
	

	public LaborTutor getLaborTutor() {
		return laborTutor;
	}

	public void setLaborTutor(LaborTutor laborTutor) {
		this.laborTutor = laborTutor;
	}

	/**
	 * hashcode de la práctica
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Equals de la práctica
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Practise other = (Practise) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * Muestra información de la práctica
	 */
	@Override
	public String toString() {
		return "Practise [id=" + id + ", start=" + start + ", finish=" + finish + ", enrollment=" + enrollment
				+ ", business=" + business + "]";
	}

}

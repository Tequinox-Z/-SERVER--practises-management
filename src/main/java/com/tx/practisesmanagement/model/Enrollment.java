package com.tx.practisesmanagement.model;



import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * Clase de matriculación de un estudiante
 * @author Salva
 */
@Entity
public class Enrollment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;									// Identificador de la matricula
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date date;									// Fecha de matriculación
	
	@ManyToOne
	private Student student;							// Estudiante 
	
	@OneToMany(mappedBy = "enrollment")
	@JsonIgnore
	private List<Preference> preferences;				// Preferencias
	
	@OneToOne
	private Practise practise;							// Práctica
	
	@ManyToOne
	private ProfessionalDegree professionalDegree;		// Ciclo matriculado
	
	
	/**
	 * Obtiene las preferencias del alumno
	 * @return Lista de preferencias
	 */
	public List<Preference> getPreferences() {
		return preferences;
	}

	/**
	 * Constructor sin parámetros
	 */
	public Enrollment() {
		super();
	}
	
	/**
	 * Constructor con fecha
	 * @param date Fecha de matriculación
	 */
	public Enrollment(Date date) {
		super();
		this.date = date;
	}
	
	
	/**
	 * Constructor con todos los parámetros
	 * @param date: Fecha
	 * @param student: Estudiante
	 * @param practise: Práctica
	 * @param professionalDegree: Ciclo
	 */
	public Enrollment(Date date, Student student, Practise practise, ProfessionalDegree professionalDegree) {
		super();
		this.date = date;
		this.student = student;
		this.practise = practise;
		this.professionalDegree = professionalDegree;
	}
	
	
	
	/**
	 * Establece el identificador
	 * @param id: Nuevo id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Constructor con fecha, estudiante y ciclo
	 * @param date
	 * @param student
	 * @param professionalDegree
	 */
	public Enrollment(Date date, Student student, ProfessionalDegree professionalDegree) {
		super();
		// Asignamos los datos
		this.date = date;
		this.student = student;
		this.professionalDegree = professionalDegree;
	}

	/**
	 * Obtiene la fecha
	 * @return Fecha de matriculación
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Establece la fecha
	 * @param date: Nueva fecha
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Obtiene el estudiante
	 * @return Estudiante
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Establece el estudiante
	 * @param student: Nuevo estudiante
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * Obtiene la práctica
	 * @return Práctica
	 */
	public Practise getPractise() {
		return practise;
	}

	/**
	 * Establece la práctica
	 * @param practise: Nueva práctica
	 */
	public void setPractise(Practise practise) {
		this.practise = practise;
	}



	/**
	 * Obtiene el identificador de la matrícula
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Muestra información de la matrícula
	 */
	@Override
	public String toString() {
		return "Enrollment [id=" + id + ", date=" + date + ", student=" + student + ", practise=" + practise
			 + "]";
	}

	/**
	 * Obtiene el ciclo
	 * @return Ciclo
	 */
	public ProfessionalDegree getProfessionalDegree() {
		return professionalDegree;
	}

	/**
	 * Establece el ciclo
	 * @param professionalDegree: Nuevo ciclo
	 */
	public void setProfessionalDegree(ProfessionalDegree professionalDegree) {
		this.professionalDegree = professionalDegree;
	}

	/**
	 * HashCode de la matrícula
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Equals de matrícula
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enrollment other = (Enrollment) obj;
		return Objects.equals(id, other.id);
	} 
	
}

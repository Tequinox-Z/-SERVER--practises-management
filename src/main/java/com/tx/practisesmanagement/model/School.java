package com.tx.practisesmanagement.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Clase de escuela
 * @author Salvador
 */
@Entity
public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;										// Identificador
	private String name;									// Nombre
	private String address;									// Dirección
	private String image;									// Imagen
	private String password;								// Contraseña
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date openingTime;						// Hora de apertura
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date closingTime;						// Hora de cierre
	
	@JsonIgnore
	@OneToOne
	private Location location;								// Localización

	@JsonIgnore
	@OneToMany()
	private List<RegTemp> temperatureRecords;				// Registros de temperatura
	
	@JsonIgnore
	@OneToMany()
	private List<UnusualMovement> unusualsMovements;		// Movimientos inusuales
	
	@JsonIgnore
	@OneToMany(mappedBy = "school", fetch = FetchType.EAGER)
	private List<ProfessionalDegree> professionalDegrees;	// Ciclos

	@JsonIgnore
	@OneToMany(mappedBy = "schoolSetted")
	private List<Administrator> administrators;				// Administradores
	
	/**
	 * Constructor sin parámetros
	 */
	public School() {
		super();
	}

	/**
	 * Constructor con nombre, dirección y contraseña
	 * @param name: Nombre
	 * @param address: Dirección
	 * @param password: Contraseña
	 */
	public School(String name, String address, String password) {
		super();
		this.password = password;
		this.name = name;
		this.address = address;
	}
	
	/**
	 * Constructor con parámetros
	 * @param id Identificador
	 * @param name Nombre
	 * @param address Direccion
	 * @param image Imagen
	 * @param password Contraseña
	 * @param openingTime Horario de apertura
	 * @param closingTime Horario de cierre
	 * @param location Localización
	 * @param temperatureRecords Registros de temperatura
	 * @param unusualsMovements Movimientos inusuales
	 * @param professionalDegrees Ciclos
	 * @param administrators Administradores
	 */
	public School(Integer id, String name, String address, String image, String password, Date openingTime,
			Date closingTime, Location location, List<RegTemp> temperatureRecords,
			List<UnusualMovement> unusualsMovements, List<ProfessionalDegree> professionalDegrees,
			List<Administrator> administrators) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.image = image;
		this.password = password;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.location = location;
		this.temperatureRecords = temperatureRecords;
		this.unusualsMovements = unusualsMovements;
		this.professionalDegrees = professionalDegrees;
		this.administrators = administrators;
	}

	/**
	 * Constructor con parámetros
	 * @param name Nombre
	 * @param address Dirección
	 * @param image Imagen
	 * @param password Contraseña
	 * @param openingTime Horario de apertura
	 * @param closingTime Horario de cierre
	 * @param location Localización
	 * @param temperatureRecords Registros de temperatura
	 * @param unusualsMovements Movimientos inusuales
	 * @param professionalDegrees Ciclos
	 * @param administrators Administradores
	 */
	public School(String name, String address, String image, String password, Date openingTime,
			Date closingTime, Location location, List<RegTemp> temperatureRecords,
			List<UnusualMovement> unusualsMovements, List<ProfessionalDegree> professionalDegrees,
			List<Administrator> administrators) {
		super();
		this.name = name;
		this.address = address;
		this.image = image;
		this.password = password;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.location = location;
		this.temperatureRecords = temperatureRecords;
		this.unusualsMovements = unusualsMovements;
		this.professionalDegrees = professionalDegrees;
		this.administrators = administrators;
	}

	/**
	 * Constructor con parámetros
	 * @param name Nombre
	 * @param address Dirección
	 * @param image Imagen
	 * @param password Contraseña
	 */
	public School(String name, String address, String image, String password) {
		super();
		this.name = name;
		this.address = address;
		this.password = password;
		this.image = image;
	}
	
	/**
	 * Añade un movimiento inusual
	 * @param newMovement Nuevo movimiento
	 */
	public void addUnusualMovement(UnusualMovement newMovement) {
		this.unusualsMovements.add(newMovement);
	}

	/**
	 * Obtiene los movimientos
	 * @return Lista de movimientos inusuales
	 */
	public List<UnusualMovement> getUnusualsMovements() {
		return unusualsMovements;
	}

	/**
	 * Establece los movimientos inusuales
	 * @param unusualsMovements Lista de nuevos movimientos
	 */
	public void setUnusualsMovements(List<UnusualMovement> unusualsMovements) {
		this.unusualsMovements = unusualsMovements;
	}
	
	/**
	 * Borra todos los movimientos inusuales
	 */
	public void removeAllUnusualsMovements() {
		this.unusualsMovements.clear();
	}

	/**
	 * Constructor con todos los parámetros
	 * @param name: Nombre
	 * @param address: Dirección
	 * @param professionalDegrees: Ciclos
	 * @param administrators: Administradores
	 * @param password: Contraseña
	 */
	public School(String name, String address, List<ProfessionalDegree> professionalDegrees, List<Administrator> administrators,  String password) {
		super();
		this.password = password;
		this.name = name;
		this.address = address;
		this.professionalDegrees = professionalDegrees;
		this.administrators = administrators;
	}

	/**
	 * Obtiene el id
	 * @return: Identificador
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el id
	 * @param id: Nuevo id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre
	 * @return: Nombre
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtiene la hora de apertura
	 * @return Hora de apertura
	 */
	public Date getOpeningTime() {
		return openingTime;
	}

	/**
	 * Establece la hora de apertura
	 * @param openingTime Nueva hora de apertura
	 */
	public void setOpeningTime(Date openingTime) {
		this.openingTime = openingTime;
	}

	/**
	 * Obtiene la hora de cierre
	 * @return Hora de cierre
	 */
	public Date getClosingTime() {
		return closingTime;
	}

	/**
	 * Establece la hora de cierrre
	 * @param closingTime Hora de cierre
	 */
	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}

	/**
	 * Establece los registros de temperatura
	 * @param temperatureRecords: Nuevos registros de temperartura
	 */
	public void setTemperatureRecords(List<RegTemp> temperatureRecords) {
		this.temperatureRecords = temperatureRecords;
	}

	/**
	 * Establece el nombre
	 * @param name: Nuevo nombre
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene la dirección
	 * @return: Dirección
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Establece la dirección
	 * @param address: Nueva dirección
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Obtiene la localización
	 * @return Localización
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Establece una nueva localización
	 * @param location Nueva Localización 
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Obtiene los ciclos
	 * @return: Ciclos
	 */
	public List<ProfessionalDegree> getProfessionalDegrees() {
		return professionalDegrees;
	}
	
	/**
	 * Establece los ciclos
	 * @param professionalDegrees: Nuevos ciclos
	 */
	public void setProfessionalDegrees(List<ProfessionalDegree> professionalDegrees) {
		this.professionalDegrees = professionalDegrees;
	}

	/**
	 * Añade un nuveo registro de temperatura
	 * @param recordTemp Nuevo registro
	 */
	public void addRecordTemp(RegTemp recordTemp) {		
		this.temperatureRecords.add(recordTemp);						// Añadimos el nuevo registro
	}
	
	/**
	 * Obtiene los registros de temperatura
	 * @return Lista de registros de temperatura
	 */
	public List<RegTemp> getTemperatureRecords() {
		return temperatureRecords;
	}

	/**
	 * Obtiene la imagen
	 * @return: Imagen
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Establece la imagen
	 * @param image: Nueva imagen
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Obtiene la contraseña
	 * @return: Contraseña
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtiene los administradores
	 * @return: Administradores
	 */
	public List<Administrator> getAdministrators() {
		return administrators;
	}

	/**
	 * Establece los administradores del centro
	 * @param administrators: Nuevos administradores
	 */
	public void setAdministrators(List<Administrator> administrators) {
		this.administrators = administrators;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Hashcode de colegio
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		School other = (School) obj;
		return Objects.equals(id, other.id);
	}
	
	
	/**
	 * Muestra información del colegio
	 */
	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + ", address=" + address + ", professionalDegrees="
				+ professionalDegrees + "]";
	}
	
}

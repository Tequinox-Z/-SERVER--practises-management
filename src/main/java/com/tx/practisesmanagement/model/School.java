package com.tx.practisesmanagement.model;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Clase de escuela
 * @author Salva
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
	private LocalDateTime openingTime;
	private LocalDateTime closingTime;
	
	@JsonIgnore
	@OneToOne
	private Location location;

	@JsonIgnore
	@OneToMany
	private List<RegTemp> temperatureRecords;
	
	@JsonIgnore
	@OneToMany
	private List<UnusualMovement> unusualsMovements;
	
	@JsonIgnore
	@OneToMany(mappedBy = "school")
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
	
	public School(Integer id, String name, String address, String image, String password, LocalDateTime openingTime,
			LocalDateTime closingTime, Location location, List<RegTemp> temperatureRecords,
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

	public School(String name, String address, String image, String password, LocalDateTime openingTime,
			LocalDateTime closingTime, Location location, List<RegTemp> temperatureRecords,
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

	public School(String name, String address, String image, String password) {
		super();
		this.name = name;
		this.address = address;
		this.password = password;
		this.image = image;
	}
	
	
	public void addUnusualMovement(UnusualMovement newMovement) {
		this.unusualsMovements.add(newMovement);
	}

	public List<UnusualMovement> getUnusualsMovements() {
		return unusualsMovements;
	}

	public void setUnusualsMovements(List<UnusualMovement> unusualsMovements) {
		this.unusualsMovements = unusualsMovements;
	}
	
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

	public LocalDateTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(LocalDateTime openingTime) {
		this.openingTime = openingTime;
	}

	public LocalDateTime getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(LocalDateTime closingTime) {
		this.closingTime = closingTime;
	}

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
	
	public Location getLocation() {
		return location;
	}

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

	public void addRecordTemp(RegTemp recordTemp) {
		if (this.temperatureRecords.size() > 9) {						// Almacenaremos 10 registros como máximo, si tenemos más de 9 ...
			this.temperatureRecords.remove(0);							// Borramos el primero que se añadió
		}
		
		this.temperatureRecords.add(recordTemp);						// Añadimos el nuevo registro
	}
	
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

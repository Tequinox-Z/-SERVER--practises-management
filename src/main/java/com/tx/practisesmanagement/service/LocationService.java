package com.tx.practisesmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.repository.LocationRepository;

@Service
/**
 * Servicio de localización
 * @author Salvador
 *
 */
public class LocationService {

	// Repositirios
	
	@Autowired LocationRepository locationRepository;
	
	
	/**
	 * Guarda una ubicación
	 * @param newLocation Nueva ubicación
	 * @return UBicación guardada
	 */
	public Location saveLocation(Location newLocation) {
		return this.locationRepository.save(newLocation);
	}
	
	/**
	 * Obtiene todas las localizaciones por nombre de la empresa
	 * @param name Nombre de las empresas
	 * @return Lista de localizaciones
	 */
	public List<Location> getAllBusinessLocationsAndName(String name) {
		return this.locationRepository.getAllLocationByName(name.toUpperCase());
	}
	
	/**
	 * Obtiene todas las localizaciones de la empresa
	 * @return Lista de localizaciones
	 */
	public List<Location> getAllBusinessLocations() {
		return this.locationRepository.getAllLocationBusineses();
	}
	
	/**
	 * Obtiene una localización por su id
	 * @param id Id de la localizacion
	 * @return Localizacion solicitada
	 */
	public Location getLocationById(Integer id) {
		return this.locationRepository.findById(id).orElse(null);
	}
	
	/**
	 * Obtiene las localizaciones de centros que esten dentro de un cuadro especifico de coordenadas
	 * @param maxLatitude Longitud maxima
	 * @param minLatitude Latitud máxima
	 * @param maxLongitude Longitud máxima
	 * @param minLongitude Longitud minima
	 * @return Lista de localizaciones
	 */
	public List<Location> getAllSchoolsLocationsByLocation(Double maxLatitude, Double minLatitude, Double maxLongitude, Double minLongitude) {
		return this.locationRepository.getAllLocationSchoolsByUbication(maxLatitude, minLatitude, maxLongitude, minLongitude);
	}
	/**
	 * Obtiene las localizaciones de empresas que esten dentro de un cuadro especifico de coordenadas
	 * @param maxLatitude Latitud maxima
	 * @param minLatitude Latitud minima
	 * @param maxLongitude Longitud maxima
	 * @param minLongitude Longitud minima
	 * @return Lista de localizaciones
	 */
	public List<Location> getAllBusinessLocationsByLocation(Double maxLatitude, Double minLatitude, Double maxLongitude, Double minLongitude) {
		return this.locationRepository.getAllLocationBusinesesByUbication(maxLatitude, minLatitude, maxLongitude, minLongitude);
	}
	
	/**
	 * Obtiene todas las localizaciones de los centros
	 * @return Lista de localizaciones
	 */
	public List<Location> getAllShoolsLocations() {
		return this.locationRepository.getAllLocationSchools();
	}
	

	/**
	 * Borra una localización
	 * @param id Identificado de la localización
	 */
	public void removeLocation(Integer id) {
		this.locationRepository.deleteById(id);
	}
	
	/**
	 * Edita una localización
	 * @param id Id de la localización
	 * @param newLongitude Nueva longitud
	 * @param newLatitude Nueva latitud
	 * @return Localización actualizada
	 * @throws UserErrorException Error en caso de que no exista la localización
	 */
	public Location editLocation(Integer id, Double newLongitude, Double newLatitude) throws UserErrorException {
		
		Location currentLocation = this.locationRepository.findById(id).orElse(null);			// Obtenemos la localización 
		
		// SI existe establecemos los datos
		if (currentLocation != null) {
			
			currentLocation.setLatitude(newLatitude);
			currentLocation.setLongitude(newLongitude);
			
			return locationRepository.save(currentLocation);
		}
		else {
			throw new UserErrorException("No existe esta localización");    		// Si no existe lanzamos excepción
		}
		
	}
	
}

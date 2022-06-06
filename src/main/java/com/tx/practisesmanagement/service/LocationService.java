package com.tx.practisesmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.repository.LocationRepository;

@Service
public class LocationService {

	@Autowired LocationRepository locationRepository;
	
	public Location saveLocation(Location newLocation) {
		return this.locationRepository.save(newLocation);
	}
	
	public List<Location> getAllBusinessLocationsAndName(String name) {
		return this.locationRepository.getAllLocationByName(name.toUpperCase());
	}
	
	public List<Location> getAllBusinessLocations() {
		return this.locationRepository.getAllLocationBusineses();
	}
	
	public Location getLocationById(Integer id) {
		return this.locationRepository.findById(id).orElse(null);
	}
	
	public List<Location> getAllSchoolsLocationsByLocation(Double maxLatitude, Double minLatitude, Double maxLongitude, Double minLongitude) {
		return this.locationRepository.getAllLocationSchoolsByUbication(maxLatitude, minLatitude, maxLongitude, minLongitude);
	}
	public List<Location> getAllBusinessLocationsByLocation(Double maxLatitude, Double minLatitude, Double maxLongitude, Double minLongitude) {
		return this.locationRepository.getAllLocationBusinesesByUbication(maxLatitude, minLatitude, maxLongitude, minLongitude);
	}
	
	public List<Location> getAllShoolsLocations() {
		return this.locationRepository.getAllLocationSchools();
	}
	

	
	public void removeLocation(Integer id) {
		this.locationRepository.deleteById(id);
	}
	
	public Location editLocation(Integer id, Double newLongitude, Double newLatitude) throws UserErrorException {
		
		Location currentLocation = this.locationRepository.findById(id).orElse(null);
		
		if (currentLocation != null) {
			
			currentLocation.setLatitude(newLatitude);
			currentLocation.setLongitude(newLongitude);
			
			return locationRepository.save(currentLocation);
		}
		else {
			throw new UserErrorException("No existe esta localización");
		}
		
	}
	
}

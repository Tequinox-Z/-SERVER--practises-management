package com.tx.practisesmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.repository.RegTempRepository;

@Service
/**
 * Clase de registro de temperatura
 * @author Salvador
 */
public class RegTempService {

	@Autowired RegTempRepository regTempRepository;
	/**
	 * Guarda el registro de temperatura para posteriormente ser asignado al centro
	 * @param newRegTemp Nuevo registro de temperatura
	 * @param idSchool Id de la escuela
	 * @return REgistro guardado
	 */
	public RegTemp save(RegTemp newRegTemp, Integer idSchool) {
	
		// Obtenemos la fecha actual
		
			LocalDateTime currentTime = LocalDateTime.now();
		
		// Buscamos un registro de temperatura con la misma fecha y hora
		
			RegTemp currentRegTemp = regTempRepository.getByDayAndHour(currentTime.getDayOfMonth(), currentTime.getMonthValue(), currentTime.getYear(), currentTime.getHour(), idSchool);
		
		// Comprobamos si existe
		
			if (currentRegTemp == null) {
				
				// Si no existe lo almacenamos
				
				return this.regTempRepository.save(newRegTemp);
			}
			else {
				
				// Si existe aplicamos la media a todos los datos
				
					currentRegTemp.setCelcius((currentRegTemp.getCelcius() + newRegTemp.getCelcius()) / 2);
					currentRegTemp.setFahrenheit((currentRegTemp.getFahrenheit() + newRegTemp.getFahrenheit()) / 2);
					currentRegTemp.setHeatIndexc((currentRegTemp.getHeatIndexc() + newRegTemp.getHeatIndexc()) / 2);
					currentRegTemp.setHeatIndexf((currentRegTemp.getHeatIndexf() + newRegTemp.getHeatIndexf()) / 2);
					currentRegTemp.setHumidity((currentRegTemp.getHumidity() + newRegTemp.getHumidity()) / 2);
					
				// Guardamos los cambios
					
					return this.regTempRepository.save(currentRegTemp);
			}
	}
	
	
	/**
	 * Obtiene todos los registros de temperatura de un día
	 * @param date Fecha de los registros
	 * @param idSchool Id de la escuela
	 * @return Lista de registros
	 */
	public List<RegTemp> getAllRegTempForDay(LocalDateTime date, Integer idSchool) {
		return regTempRepository.getByDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear(), idSchool);
	}
	
	
	/**
	 * Borra un registro de temperatura
	 * @param id Id del registro
	 */
	public void remove(Integer id) {
		this.regTempRepository.deleteById(id);
	}
	
	
}

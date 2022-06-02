package com.tx.practisesmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.repository.RegTempRepository;

@Service
public class RegTempService {

	@Autowired RegTempRepository regTempRepository;
	/**
	 * Guarda el registro de temperatura para posteriormente ser asignado al centro
	 * @param newRegTemp
	 * @param idSchool
	 * @return
	 */
	public RegTemp save(RegTemp newRegTemp, Integer idSchool) {
	
		LocalDateTime currentTime = LocalDateTime.now();
		RegTemp currentRegTemp = regTempRepository.getByDayAndHour(currentTime.getDayOfMonth(), currentTime.getMonthValue(), currentTime.getYear(), currentTime.getHour(), idSchool);
				
		if (currentRegTemp == null) {
			return this.regTempRepository.save(newRegTemp);
		}
		else {
			
			// Aplicamos la media a todos los datos
			
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
	 * @param date
	 * @param idSchool
	 * @return
	 */
	public List<RegTemp> getAllRegTempForDay(LocalDateTime date, Integer idSchool) {
		return regTempRepository.getByDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear(), idSchool);
	}
	
	
	/**
	 * Borra un registro de temperatura
	 * @param id
	 */
	public void remove(Integer id) {
		this.regTempRepository.deleteById(id);
	}
	
	
}

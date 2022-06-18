package com.tx.practisesmanagement.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Service;

/**
 * Servicio de funciones adicionales
 * @author Salva
 */
@Service
public class AditionalsFunctionsService {

	/**
	 * Desencripta cifrado MD5
	 * @param hash: Hash
	 * @return Texto descifrado
	 * @throws IOException
	 */
	public String desencryptMd5(String hash) throws IOException {
		String result = "";					// Esta variable almacenará el resultado
		
    	String url = "https://md5decrypt.net/en/Api/api.php?hash=" + hash + "&hash_type=md5&code=c53a3f76c5ca6e80&email=txaxati2018@gmail.com";    // Url que nos transformará el texto
		
		URL md5online = new URL(url);			// Inicializamos la url
	    BufferedReader in = new BufferedReader(new InputStreamReader(md5online.openStream()));		//Pedimos el resultado en string
	    String inputLine;
	    
	    while ((inputLine = in.readLine()) != null) {												// Mientras haya lineas ...
	      result = result + inputLine;																	// Añadimos linea a result
	    }
	    in.close();																					// Cerramos el flujo de datos
	        
	    return result;																				// Retornamos el string descifrado
	}
	
}
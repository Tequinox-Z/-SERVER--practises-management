package com.tx.practisesmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.File;
import com.tx.practisesmanagement.service.FileService;

@RestController
@CrossOrigin("https://practises-management.com")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@PostMapping("/upload")
	public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
	    
		if (file.isEmpty() || file.getSize() == 0) {
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		  		  new RestError(HttpStatus.BAD_REQUEST, "El fichero está vacío")
		    );
		}
		
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(fileService.saveFile(file));
	    } 
	    catch (Exception e) {
	    	return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
	    		  new RestError(HttpStatus.EXPECTATION_FAILED, "Error al subir fichero")
	    	);
	    }
	}
	
	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		
		File file = fileService.getFile(id);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.set("content-type", 
	      file.getMimeType());

		return ResponseEntity.ok()
				.headers(responseHeaders)
				.body(file.getData());
	}
	
	@ControllerAdvice
	public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {
	  @ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<RestError> handleMaxSizeException(MaxUploadSizeExceededException exc) {
	    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new RestError(HttpStatus.EXPECTATION_FAILED, "Fichero demasiado grande"));
	  }
	}
}

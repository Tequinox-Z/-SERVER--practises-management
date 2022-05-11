package com.tx.practisesmanagement.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tx.practisesmanagement.model.File;
import com.tx.practisesmanagement.repository.FileRepository;

@Service
public class FileService {
	
	@Autowired
	private FileRepository fileRepository;
	
	public File saveFile(MultipartFile fileToSave) throws IOException {
		File newFile = new File (
								fileToSave.getOriginalFilename(), 
								fileToSave.getContentType(), 
								fileToSave.getBytes(), 
								fileToSave.getSize()
								);
		return fileRepository.save(newFile);
	}
	
	public File removeFile(String id) {
		
		return null;
	}
	
	public File editFile(String id, MultipartFile newFile) {
		return null;
	}
	
	public File getFile(String id) {
		return fileRepository.findById(id).orElse(null);
	}
	
}

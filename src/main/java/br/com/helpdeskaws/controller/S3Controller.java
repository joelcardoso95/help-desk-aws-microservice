package br.com.helpdeskaws.controller;

import java.io.IOException;
import java.net.URI;

import javax.annotation.processing.FilerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;

import br.com.helpdeskaws.services.S3Service;

@RestController
@RequestMapping("/attachment")
public class S3Controller {
	
	@Autowired
	private S3Service s3Service;
	
	@PostMapping(consumes = { "multipart/form-data" })
	public URI uploadFile(@RequestParam("file") MultipartFile file) throws FilerException {
		return s3Service.uploadFile(file);
	}
	
	@GetMapping
	public ResponseEntity<S3Object> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
		S3Object file = s3Service.downloadFile(fileName);
		return ResponseEntity.ok().body(file);
	}
}

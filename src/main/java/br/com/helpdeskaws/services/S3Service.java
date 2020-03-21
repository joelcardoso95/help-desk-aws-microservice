package br.com.helpdeskaws.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.processing.FilerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class S3Service {
	
	private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;
	
	@Value("help-desk-bucket")
	private String bucketName;
	
	public S3Object downloadFile(String fileName) throws IOException {
		return s3client.getObject(bucketName, fileName);
	}
	
	public URI uploadFile(MultipartFile file) throws FilerException {
		try {
			
			String fileName = file.getOriginalFilename();
			InputStream is;
			is = file.getInputStream();
			String contentType = file.getContentType();
			return uploadFile(fileName, is, contentType);
		}catch (IOException e) {
			throw new FilerException("Erro de IO" + e.getMessage());
		}
	}
	
	public URI uploadFile(String fileName, InputStream is, String contentType) throws FilerException {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload realizado");
			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FilerException("Erro ao converter URL para URI " + e.getMessage());
		}
	}
}

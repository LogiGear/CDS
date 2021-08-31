package com.logigear.crm.employees.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

import com.logigear.crm.employees.model.EmployeeDetails;
import com.logigear.crm.employees.security.JwtProvider;
import com.logigear.crm.employees.util.MessageQueueAmongClasses;
import com.logigear.crm.employees.payload.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.logigear.crm.employees.config.AppProperties;
import com.logigear.crm.employees.exception.FileException;
import com.logigear.crm.employees.model.File;
import com.logigear.crm.employees.payload.UpdateStatus;
import com.logigear.crm.employees.repository.FileRepository;
import com.logigear.crm.employees.response.FileResponse;
import java.util.NoSuchElementException;

@Service
public class FileService {
	private final FileRepository fileRepository;
	private final Path fileStorageLocation;
	private final JwtProvider tokenProvider;
	private final EmployeeService employeeService;

	@Autowired
	public FileService(FileRepository fileRepository, AppProperties fileStorageProperties, JwtProvider tokenProvider,
			EmployeeService employeeService) {
		this.fileRepository = fileRepository;
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		System.out.println("File storage location: " + this.fileStorageLocation);
		this.tokenProvider = tokenProvider;
		this.employeeService = employeeService;
	}

	public FileResponse save(MultipartFile file, Long employeeId, Long userId) throws IOException {
		File fileEntity = new File();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		try {
			if (fileName.contains("..")) {
				throw new FileException("Sorry! Filename contains invalid path sequence" + fileName);
			}
			if (fileName.isEmpty()) {
				throw new FileException("Sorry! Filename is null" + fileName);
			}
			if (extension.equals("docx") == false && extension.equals("doc") == false) {
				throw new FileException("Sorry! The file is in the wrong format" + fileName);
			} else {
				fileEntity.setFileType(file.getContentType());
				fileEntity.setUploadAt(Instant.now());
				fileEntity.setId(employeeId);
				EmployeeDetails employeeDetailsFile = employeeService.getEmployeeDetailsById(employeeId);
				EmployeeDetails employeeDetailsUpload = employeeService.getEmployeeDetailsById(employeeId);
				fileName = "Resume-" + employeeId + "-" + employeeDetailsFile.getFullName() + ".doc";
				fileEntity.setFileName(fileName);
				fileEntity.setStatus("To be reviewed");
				fileEntity.setUploadBy(userId);
				fileRepository.save(fileEntity);
				Path targetLocation = this.fileStorageLocation.resolve(fileName);
				if(!Files.isDirectory(targetLocation)) {
					throw new FileException("Directory " + targetLocation + " does not exists!");
				}
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			throw new FileException("Could not store file " + fileName + ". Please try again!", e);
		}
		return new FileResponse(fileEntity.getId(), fileEntity.getFileName(), fileEntity.getStatus(),
				fileEntity.getUploadAt(), fileEntity.getNote());

	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new FileException("File not found " + fileName, ex);
		}
	}

	public FileResponse getFileDetail(Long id) {
		File file = fileRepository.findById(id).orElseThrow(NoSuchElementException::new);
		return new FileResponse(file.getId(), file.getFileName(), file.getStatus(), file.getUploadAt(), file.getNote());
	}

	public FileResponse updateStatus(Long id, UpdateStatus status) {
		File file = fileRepository.findById(id).orElseThrow(NoSuchElementException::new);
		file.setStatus(status.getStatus());
		file.setNote(status.getNote());
		fileRepository.save(file);
		return new FileResponse(file.getId(), file.getFileName(), file.getStatus(), file.getUploadAt(), file.getNote());
	}
}

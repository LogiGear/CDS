package com.logigear.crm.employees.controller;

import java.io.FileNotFoundException;
import java.time.Duration;

import javax.servlet.http.HttpServletRequest;

import com.logigear.crm.employees.payload.UpdateStatus;
import com.logigear.crm.employees.service.FileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/employees/api/employees")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	private final FileService fileService;

	private final Bucket bucket;

	@Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
		Bandwidth limit = Bandwidth.classic(1, Refill.greedy(10, Duration.ofSeconds(60)));
		this.bucket = Bucket4j.builder().addLimit(limit).build();
	}

	// @PostMapping("/upload-file")
	// @ResponseBody
	// public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
	// try {
	// if (bucket.tryConsume(1)) {
	// return ResponseEntity.status(HttpStatus.OK).body(String.format("%s",
	// fileService.save(file)));
	// }
	// return
	// ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(String.format("Too
	// many Request!"));
	// } catch (Exception e) {
	// return ResponseEntity.badRequest().build();
	// }
	// }

	@PostMapping("/upload-file/{id}")
	@ResponseBody
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("idUser") Long userId,
			@PathVariable Long id) {
		try {
			if (bucket.tryConsume(1)) {
				return ResponseEntity.status(HttpStatus.OK).body(fileService.save(file, id, userId));
			}
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many Request!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/download-file/{fileName:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request)
			throws FileNotFoundException {
		// Load file as Resource
		Resource resource = fileService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {

			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

		} catch (Exception e) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		if (bucket.tryConsume(1)) {
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		}
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(String.format("Too many Request!"));

	}

	@GetMapping("/file/{id}")
	public ResponseEntity<?> getFileDetails(@PathVariable Long id) {
		return ResponseEntity.ok().body(fileService.getFileDetail(id));
	}

	@PatchMapping("/file/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateStatus status) {
		return ResponseEntity.ok().body(fileService.updateStatus(id, status));
	}

}

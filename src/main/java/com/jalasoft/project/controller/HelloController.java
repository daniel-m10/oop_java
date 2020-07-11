package com.jalasoft.project.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.server.ExportException;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private static final String PATH = "src/main/resources/uploadedFile";


    @PostMapping
    public String sayHello(@RequestParam(value = "name") final String name,
                           @RequestParam(value = "lastName") final String lastName,
                           @RequestParam(value = "file") final MultipartFile file) throws Exception {
        final String message = "Hello %s %s, you're file \"%s\" was uploaded to the server.";
        createDirectory();
        copyFile(file);
        return String.format(message, name, lastName, file.getOriginalFilename());
    }

    private void createDirectory() throws Exception {
        try {
            Files.createDirectory(Paths.get(PATH).toAbsolutePath().normalize());
        } catch (IOException e) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    private void copyFile(final MultipartFile file) throws IOException {
        try {
            final String fileLocation = String.format("%s/%s", PATH, file.getOriginalFilename());
            Path location = Paths.get(fileLocation).toAbsolutePath().normalize();
            Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new ExportException(String.format("Could not store file %s", file.getOriginalFilename()), ex);
        }
    }
}

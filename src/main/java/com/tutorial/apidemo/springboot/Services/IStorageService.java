package com.tutorial.apidemo.springboot.Services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    public String storeFile(MultipartFile file);
    public Stream<Path> loadAll();//load all file inside the folder
    public byte[] readFileContent(String filename);
    public void deleteAllFiles();
}

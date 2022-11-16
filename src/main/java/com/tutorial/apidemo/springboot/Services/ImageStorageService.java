package com.tutorial.apidemo.springboot.Services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;
@Service
public class ImageStorageService  implements IStorageService{
    private final Path storageFolder = Paths.get("uploads");
    //constructor
    public ImageStorageService(){
        try {
            Files.createDirectories(storageFolder);
        }catch (IOException exception){
            throw new RuntimeException("cannot initialize storage ", exception);
        }
    }
    private boolean isImageFile(MultipartFile file){
        //let install filenameutils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg","bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("haha");
            if(file.isEmpty()){
                throw new RuntimeException("Failed to store empty file ");
            }
            //check file is image
            if(!isImageFile(file)){
                throw new RuntimeException("You can only upload image file ");
            }
            //file must be <= 5mb
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if(fileSizeInMegabytes>5.0f){
                throw new RuntimeException("FIle must be lower 5Mb  ");
            }
            //file must be rename
            String fileExtension =FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFilename= UUID.randomUUID().toString().replace("-","");
            generatedFilename =generatedFilename +"."+fileExtension;
            Path destinationFilePath=this.storageFolder.resolve(Paths.get(generatedFilename)).normalize().toAbsolutePath();
            if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
                //this is seurity check
                throw new RuntimeException("Canot store file outside current directory. ");
            }
            try(InputStream inputStream=file.getInputStream()){
                Files.copy(inputStream,destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFilename;
        }catch (IOException exception)
        {
            throw new RuntimeException("cannot initialize storage ", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {

        try {
            //list all files in storage folder
            return Files.walk(this.storageFolder,1)
                    .filter(path ->!path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);

        }catch (IOException e){
            throw new RuntimeException("failed to load Storage files",e);
        }
    }

    @Override
    public byte[] readFileContent(String filename) {
        try {
            Path file = storageFolder.resolve(filename);
            Resource resource =new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                byte[] bytes= StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException("could not read file: "+filename);
            }


        }catch (IOException exception){
            throw new RuntimeException("could not read file: "+filename,exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}

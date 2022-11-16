package com.tutorial.apidemo.springboot.Controllers;

import com.tutorial.apidemo.springboot.Models.ResponseObject;
import com.tutorial.apidemo.springboot.Services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/api/v1/FileUpLoad")
public class FileUploadController {
    //This controller receive file/image from client
    //inject Storage service here
    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            //save file to a folder => service
            String generatedFilename=storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","upload file successfully",generatedFilename)
            );
            //47297aa03bd84f62b02f4332f5cbf69e.jpg -> how to open this project in Web Browser
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("ok",exception.getMessage(),"")
            );
        }
    }
    @GetMapping("/files/{fileName:.+}")
    // /files/47297aa03bd84f62b02f4332f5cbf69e.jpg
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName){
        try {
            byte[] bytes =storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }catch (Exception exception){
            return ResponseEntity.noContent().build();
        }
    }
    // how to load all uploaded file
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadFile(){
        try {
            List<String> urls =storageService.loadAll().map(path -> {
                //convert filename to url(send request "read)
                String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "readDetailFile",path.getFileName().toString()).build().toUri().toString();
                return urlPath;
            })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("ok","List files successfully",urls));
        }catch (Exception e){
            return ResponseEntity.ok(new ResponseObject("failed","List files failed",new String[] {}));
        }
    }

}

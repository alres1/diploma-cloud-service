package ru.netology.diplomacloudservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomacloudservice.dto.UpdateFileRequest;
import ru.netology.diplomacloudservice.dto.UploadFileResponse;
import ru.netology.diplomacloudservice.service.FileService;

import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class FileController {

    @Autowired
    private FileService fileService;


    @PostMapping("/file")
    public UploadFileResponse uploadFile(@RequestHeader("auth-token") String authToken, MultipartFile file) {
        return fileService.uploadFile(file, authToken);
    }

    @PutMapping("/file")
    public UploadFileResponse updateFile(@RequestParam("filename") String fileName, @RequestBody UpdateFileRequest newName, @RequestHeader("auth-token") String authToken) {
        return fileService.updateFile(fileName, newName, authToken);
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestParam("filename") String fileName, @RequestHeader("auth-token") String authToken) {
        fileService.deleteFile(fileName, authToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestParam("filename") String fileName, @RequestHeader("auth-token") String authToken) {
        byte[] filebyte = fileService.getFileByNameAndMyUser(fileName, authToken);
        return ResponseEntity.ok().body(new ByteArrayResource(filebyte));
    }

    @GetMapping("/list")
    public List<UploadFileResponse> getFileList(@RequestHeader("auth-token") String authToken){
        return fileService.getFileList(authToken);
    }

}

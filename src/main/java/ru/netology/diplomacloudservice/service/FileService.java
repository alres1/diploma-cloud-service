package ru.netology.diplomacloudservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomacloudservice.dto.UpdateFileRequest;
import ru.netology.diplomacloudservice.dto.UploadFileResponse;
import ru.netology.diplomacloudservice.entity.FileSt;
import ru.netology.diplomacloudservice.entity.MyUser;
import ru.netology.diplomacloudservice.exceptions.FileStNotFoundException;
import ru.netology.diplomacloudservice.exceptions.InputDataException;
import ru.netology.diplomacloudservice.exceptions.UnauthorizedException;
import ru.netology.diplomacloudservice.repository.AuthRepository;
import ru.netology.diplomacloudservice.repository.FileRepository;
import ru.netology.diplomacloudservice.repository.MyUserRepository;


import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FileService {

    private FileRepository fileRepository;
    private AuthRepository authRepository;
    private MyUserRepository myUserRepository;

    //Upload file
    public UploadFileResponse uploadFile(MultipartFile file, String authToken)  {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            log.error("User unauthorized");
            throw new UnauthorizedException("User unauthorized");
        }
        try {
            String fileName = file.getOriginalFilename();
            FileSt fileSt = new FileSt(UUID.randomUUID().toString(), fileName, file.getContentType(), file.getSize(), file.getBytes(), myUser);
            fileRepository.save(fileSt);
            log.info("Success upload file "+fileSt.getName());
            return mapToFileResponse(fileSt);
        }catch (IOException ex){
            log.error("Error input data. User "+myUser.getLogin());
            throw new InputDataException("Error input data");
        }
    }

    //Update file
    @Transactional
    public UploadFileResponse updateFile(String fileName, UpdateFileRequest newName, String authToken) {

        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            log.error("User unauthorized");
            throw new UnauthorizedException("User unauthorized");
        }
        //Проверка на существование файла для изменения
        FileSt oldFileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (oldFileSt == null) {
            log.error("File "+fileName+" not found");
            throw new FileStNotFoundException("File not found");
        }
        fileRepository.updateNameByNameAndMyUser(newName.getFilename(), fileName, myUser);
        //Проверка на успешное обновление
        FileSt newFileSt = fileRepository.findByNameAndMyUser(newName.getFilename(), myUser);
        if (newFileSt == null) {
            log.error("File "+newName.getFilename()+" has not been updated");
            throw new FileStNotFoundException("File has not been updated");
        }
        log.info("Success update file "+newFileSt.getName());
        return  mapToFileResponse(newFileSt);
    }

    //Delete file
    @Transactional
    public boolean deleteFile(String fileName, String authToken) {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            log.error("User unauthorized");
            throw new UnauthorizedException("User unauthorized");
        }
        //Проверка на существование файла для изменения
        FileSt oldFileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (oldFileSt == null) {
            log.error("File " + fileName + " not found");
            throw new FileStNotFoundException("File not found");
        }
        try {
            fileRepository.deleteByNameAndMyUser(fileName, myUser);
        } catch (FileStNotFoundException ex){
            log.error("File "+fileName+" has not been deleted");
            throw new FileStNotFoundException("File has not been deleted");
        }
        //Проверка удаления файла
//        FileSt fileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
//        if (fileSt != null) {
//            log.error("File "+fileName+" has not been deleted");
//            throw new FileStNotFoundException("File has not been deleted");
//        }
        log.info("Success delete file "+fileName);
        return true;
    }

    //Get file
    public byte[] getFileByNameAndMyUser(String fileName, String authToken) {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            log.error("User unauthorized");
            throw new UnauthorizedException("User unauthorized");
        }
        final FileSt fileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (fileSt == null) {
            log.error("File "+fileSt.getName()+" not found");
            throw new FileStNotFoundException("File not found");
        }
        log.info("Success get file "+fileSt.getName());
        return fileSt.getData();
    }

    //Get list
    public List<UploadFileResponse> getFileList(String authToken){
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            log.error("User unauthorized");
            throw new UnauthorizedException("User unauthorized");
        }
        log.info("Success get all files. User "+myUser.getLogin());
        return fileRepository.findAllByMyUser(myUser)
                .stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }





    private UploadFileResponse mapToFileResponse(FileSt fileSt) {
        return new UploadFileResponse(fileSt.getName(), fileSt.getSize());
    }

    private MyUser getUserByToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final String authTokenWithoutBearer = authToken.split(" ")[1];
            final String username = authRepository.getUserLoginByToken(authTokenWithoutBearer);
            return myUserRepository.findByLogin(username);
        }
        return null;
    }
}
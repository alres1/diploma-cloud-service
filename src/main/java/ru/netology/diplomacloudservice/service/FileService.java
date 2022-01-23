package ru.netology.diplomacloudservice.service;

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
public class FileService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private MyUserRepository myUserRepository;

    //Upload file
    public UploadFileResponse store(MultipartFile file, String authToken)  {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            throw new UnauthorizedException("User unauthorized");
        }
        try {
            String fileName = file.getOriginalFilename();
            FileSt fileSt = new FileSt(UUID.randomUUID().toString(), fileName, file.getContentType(), file.getSize(), file.getBytes(), myUser);
            fileRepository.save(fileSt);
            return mapToFileResponse(fileSt);
        }catch (IOException ex){
            throw new InputDataException("Error input data");
        }
    }

    //Update file
    @Transactional
    public UploadFileResponse updateFile(String fileName, UpdateFileRequest newName, String authToken) {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            throw new UnauthorizedException("User unauthorized");
        }
        //Проверка на существование файла для изменения
        FileSt oldFileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (oldFileSt == null) {
            throw new FileStNotFoundException("File not found");
        }
        fileRepository.updateNameByNameAndMyUser(newName.getName(), fileName, myUser);
        //Проверка на успешное обновление
        FileSt newFileSt = fileRepository.findByNameAndMyUser(newName.getName(), myUser);
        if (newFileSt == null) {
            throw new FileStNotFoundException("File has not been updated");
        }
        return  mapToFileResponse(newFileSt);
    }

    //Delete file
    @Transactional
    public boolean deleteFile(String fileName, String authToken) {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            throw new UnauthorizedException("User unauthorized");
        }
        //Проверка на существование файла для изменения
        FileSt oldFileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (oldFileSt == null) {
            throw new FileStNotFoundException("File not found");
        }
        fileRepository.deleteByNameAndMyUser(fileName, myUser);
        //Заменить вывод в ответе
        FileSt fileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (fileSt != null) {
            throw new FileStNotFoundException("File has not been deleted");
        }
        return true;
    }

    //Get file
    public FileSt getFileByNameAndMyUser(String fileName, String authToken) {
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            throw new UnauthorizedException("User unauthorized");
        }
        final FileSt fileSt = fileRepository.findByNameAndMyUser(fileName, myUser);
        if (fileSt == null) {
            throw new FileStNotFoundException("File not found");
        }
        return fileSt;
    }

    //Get list
    public List<UploadFileResponse> getFileList(String authToken){
        final MyUser myUser = getUserByToken(authToken);
        if (myUser == null) {
            throw new UnauthorizedException("User unauthorized");
        }
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
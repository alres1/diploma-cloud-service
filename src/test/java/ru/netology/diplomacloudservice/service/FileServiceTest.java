package ru.netology.diplomacloudservice.service;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomacloudservice.dto.UpdateFileRequest;
import ru.netology.diplomacloudservice.dto.UploadFileResponse;
import ru.netology.diplomacloudservice.entity.FileSt;
import ru.netology.diplomacloudservice.entity.MyUser;
import ru.netology.diplomacloudservice.exceptions.UnauthorizedException;
import ru.netology.diplomacloudservice.repository.AuthRepository;
import ru.netology.diplomacloudservice.repository.FileRepository;
import ru.netology.diplomacloudservice.repository.MyUserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileServiceTest {

    //User
    final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY0MzU2OTMzOCwiaWF0IjoxNjQzNDQ5MzM4LCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiJ9.iobQv_IsQbxqKH8CM5AJy71Bg9kClCqdkaco-e9G9vA";
    final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY0MzU2OTMzOCwiaWF0IjoxNjQzNDQ5MzM4LCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiJ9.iobQv_IsQbxqKH8CM5AJy71Bg9kClCqdkaco-e9G9vA";
    final String USERNAME = "User1";
    final MyUser MY_USER = new MyUser(1L,"User1", "Password", "USER");

    ///// Files
    final String FILENAME1 = "FileName1";
    final String NEWFILENAME = "NewFileName";
    final byte[] CONTENT1 = FILENAME1.getBytes();
    final MultipartFile MULTIPART_FILE = new MockMultipartFile(FILENAME1, CONTENT1);
    final FileSt FILE1 = new FileSt("1",FILENAME1,"png", 1000L, CONTENT1, MY_USER);
    final FileSt FILE1NEW = new FileSt("1",NEWFILENAME,"png", 1000L, CONTENT1, MY_USER);
    final UpdateFileRequest UPDATE_FILE_REQ = new UpdateFileRequest(NEWFILENAME);

    final String FILENAME2 = "FileName2";
    final byte[] CONTENT2 = FILENAME2.getBytes();
    final FileSt FILE2 = new FileSt("2",FILENAME2,"jpg", 1500L, CONTENT2, MY_USER);

    final UploadFileResponse UPLOAD_FILE_RESP1 = new UploadFileResponse(FILENAME1,1000L);
    final UploadFileResponse UPLOAD_FILE_RESP2 = new UploadFileResponse(FILENAME2,1500L);
    final List<FileSt> FILE_LIST = List.of(FILE1, FILE2);
    final List<UploadFileResponse> RETURN_FILE_LIST = List.of(UPLOAD_FILE_RESP1, UPLOAD_FILE_RESP2);

    @InjectMocks
    private FileService fileService;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private MyUserRepository myUserRepository;

    @Before
    void init() {
        authRepository.putTokenAndUsername(TOKEN, USERNAME);
    }
    @BeforeEach
    void setUp() {
        when(authRepository.getUserLoginByToken(TOKEN)).thenReturn(USERNAME);
        when(myUserRepository.findByLogin(USERNAME)).thenReturn(MY_USER);
    }

    @Test
    void uploadFile() {
        when(fileService.uploadFile(MULTIPART_FILE, BEARER_TOKEN)).thenReturn(UPLOAD_FILE_RESP1);
    }
    @Test
    void uploadFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.uploadFile(MULTIPART_FILE, ""));
    }

    @Test
    void updateFile() {
        when(fileRepository.findByNameAndMyUser(FILENAME1, MY_USER)).thenReturn(FILE1);
        when(fileRepository.findByNameAndMyUser(NEWFILENAME, MY_USER)).thenReturn(FILE1NEW);
        fileService.updateFile(FILENAME1, UPDATE_FILE_REQ, BEARER_TOKEN);
        verify(fileRepository, Mockito.times(1)).updateNameByNameAndMyUser(NEWFILENAME, FILENAME1, MY_USER);
    }
    @Test
    void updateFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.updateFile(FILENAME1, UPDATE_FILE_REQ, ""));
    }

    @Test
    void deleteFile() {
        when(fileRepository.findByNameAndMyUser(FILENAME1, MY_USER)).thenReturn(FILE1);
        fileService.deleteFile(FILENAME1, BEARER_TOKEN);
        verify(fileRepository, Mockito.times(1)).deleteByNameAndMyUser(FILENAME1, MY_USER);
    }
    @Test
    void deleteFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.deleteFile(FILENAME1, ""));
    }

    @Test
    void getFileByNameAndMyUser() {
        when(fileRepository.findByNameAndMyUser(FILENAME1, MY_USER)).thenReturn(FILE1);
        assertEquals(CONTENT1, fileService.getFileByNameAndMyUser(FILENAME1, BEARER_TOKEN));
    }
    @Test
    void getFileByNameAndMyUserUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.getFileByNameAndMyUser(FILENAME1, ""));
    }

    @Test
    void getFileList() {
        when(fileRepository.findAllByMyUser(MY_USER)).thenReturn(FILE_LIST);
        assertEquals(RETURN_FILE_LIST, fileService.getFileList(BEARER_TOKEN));
    }
    @Test
    void getFileListUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.getFileList(""));
    }
}
package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)
        throws Exception{
        UUID uuid = UUID.randomUUID(); // 랜덤으로 UUID 를 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFilename = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath+"/"+savedFilename;
        System.out.println(fileUploadFullUrl);
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFilename;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if (deleteFile.exists()){ // deleteFile 객체 여부를 확인
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}

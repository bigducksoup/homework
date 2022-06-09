package com.duck.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SaveFileService {

    boolean saveFile(MultipartFile file,String filename) throws IOException;

}

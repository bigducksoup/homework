package com.duck.homework.service;

import org.springframework.web.multipart.MultipartFile;

public interface SaveFileService {

    boolean saveFile(MultipartFile file,String filename);

}

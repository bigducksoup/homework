package com.duck.homework.service.impl;


import com.duck.homework.service.SaveFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class SaveFileServiceImpl implements SaveFileService {



    @Override
    public boolean saveFile(MultipartFile file,String filename){
        if (file.isEmpty()){
            return false;
        }


        //文件路径
        String filePath = "D:\\datas\\imgmsg\\";
        File temp = new File(filePath);
        if (!temp.exists()){
            temp.mkdirs();
        }

        File localFile = new File(filePath+filename);
        try {
            file.transferTo(localFile); //把上传的文件保存至本地
            System.out.println(file.getOriginalFilename()+" 上传成功");
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

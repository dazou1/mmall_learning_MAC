package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午1:55 18/11/14
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

//    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    //返回上传的文件名
    public String upload(MultipartFile file, String path) {
        //上传文件的原始文件名
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);//去除“.”
        //文件名,利用UUID,保证上传的文件名不重复
        String uploadName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件:上传文件的文件名是:{},上传路径:{},新的文件名:{}",fileName, path, uploadName);

        //创建目录
        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);//设置可写
            fileDir.mkdirs();
        }
        //创建文件
        File targetFile = new File(path, uploadName);
        //上传文件
        try {
            file.transferTo(targetFile);
            //文件上传已成功

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //文件已经上传到FTP服务器上

            targetFile.delete();
            //上传完成之后,将upload下面的文件删除
        } catch (IOException e) {
            log.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();//返回上传成功之后的文件名
    }
}

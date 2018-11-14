package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午1:55 18/11/14
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}

package com.ego.manage.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PicService {
    /**
     * 文件上传
     * file
     */
    Map<String, Object> upload(MultipartFile file);
}

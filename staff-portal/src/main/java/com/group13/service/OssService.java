package com.group13.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author xujinfengxu
 */
public interface OssService {

    /**
     * 上传头像到到阿里oss
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}

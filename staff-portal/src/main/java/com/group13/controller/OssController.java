package com.group13.controller;

import com.group13.common.R;
import com.group13.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xujinfengxu
 */
@RestController
@RequestMapping("/api/oss/fileoss")
@Api(tags = "Ali Cloud OSS file storage management")
@CrossOrigin
public class OssController {

    private OssService ossService;

    @Autowired
    public OssController(OssService ossService) {
        this.ossService = ossService;
    }

    /**
     * 上传头像的方法
     */
    @ApiOperation("上传头像到OSS")
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        // 获取上传文件 MultipartFile
        // 返回oss路径
        String url = ossService.uploadFileAvatar(file);
        System.out.println(url);
        return R.ok().data("url", url);
    }
}

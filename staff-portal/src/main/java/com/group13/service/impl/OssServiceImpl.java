package com.group13.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;

import com.group13.service.OssService;
import com.group13.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author xujinfengxu
 */
@Service
@Transactional
public class OssServiceImpl implements OssService {

    /**
     * 上传头像到OSS
     * @param file
     * @return
     */
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String url = null;

        try {
            // 获取文件流
            InputStream inputStream = file.getInputStream();
            // 获取文件名称 添加随机值，使得文件名都不相同
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            // 把文件按照日期进行分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath + "/" + fileName;
            // 创建PutObject请求。
            ossClient.putObject(bucketName, fileName, inputStream);
            // 需要手动拼接返回路径
            url = "https://"+bucketName+"."+endpoint+"/"+fileName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            System.out.println("IO异常");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}

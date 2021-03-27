package com.bonc.crawler.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * 自定义系统全局配置文件信息的实体映射类
 *
 * @author 兰杰
 * @create 2019-09-20 14:22
 */
@Configuration
public class GlobalSysConfig {

    @Value("${image.save-path}")
    private String imageSavePath; //图片保存地址

    @Value("${image.access-path}")
    private String imageAccessPath; //图片保存地址

    public String getImageSavePath() {
        return imageSavePath;
    }

    public void setImageSavePath(String imageSavePath) {
        this.imageSavePath = imageSavePath;
    }

    public String getImageAccessPath() {
        return imageAccessPath;
    }

    public void setImageAccessPath(String imageAccessPath) {
        this.imageAccessPath = imageAccessPath;
    }
}

#### 介绍
 blog-crawler博客采集系统，本系统分两个子系统，一个是CMS系统，一个是爬虫系统。  
 CMS系统采用 SpringBoot+Mybatis+Maven3+Shiro+Restful+Lucene+Bootstrap+Layui等技术栈  
 爬虫系统采用 HttpClient+Jsoup+Quartz+Log4j+Ehcache等技术栈  

#### 注意事项
本项目作为教学使用，请勿商用

#### 使用说明
1. 拉取项目:  
    1.1. 打开idea，选择下图所示方式，从gitee中拉取项目  
![avatar](https://images.gitee.com/uploads/images/2021/0327/105124_e75a6875_5479060.png)  
  
    1.2. 按下图所示，输入项目gitee地址[https://gitee.com/gucaini/blog-crawler.git](http://)，输入自己本地磁盘的存放目录，例如：[D:\workspace\demo\blog-crawler](http://)，点击克隆按钮Clone  
![avatar](https://images.gitee.com/uploads/images/2021/0327/110542_919b93b5_5479060.png)  
  
    1.3. 更改自己的Maven仓库，File->Settings->Build,Execution,Deployment->Build tools->Maven，如下图所示
![avatar](https://images.gitee.com/uploads/images/2021/0327/112533_9f2ce17f_5479060.png)
  
    1.4. 等待下载完毕所有依赖

                                      
2.  创建数据库：  
    2.1. 创建数据库blog-crawler，如下图所示  
![avatar](https://images.gitee.com/uploads/images/2021/0327/115524_d6adeefc_5479060.png)  
  
    2.2. 找到数据库sql文件脚本，如下图所示   
![avatar](https://images.gitee.com/uploads/images/2021/0327/115707_2c01f150_5479060.png)  
  
    2.3. 导入数据，Database->Import->Restore From SQL DUMP，如下图所示
![avatar](https://images.gitee.com/uploads/images/2021/0327/120026_ada3bdec_5479060.png)  
  
    2.4. 数据导入成功，如下图所示
![avatar](https://images.gitee.com/uploads/images/2021/0327/120304_bbb3abd5_5479060.png)  
  
3.  修改项目配置文件：  
    3.1. 修改blog项目配置文件，如下图所示，将图中第3步数据库连接地址改为自己的数据库连接地址，将图中第4步的lucene配置改为你本地的目录地址（自己创建一个lucene文件夹），将第5步中的项目图片保存地址改为你当前blog项目所在的目录地址
![avatar](https://images.gitee.com/uploads/images/2021/0327/120944_83102b00_5479060.png)  
  
    3.2. 修改crawler项目配置文件，如下图所示，将图中第3步数据库连接地址改为自己的数据库连接地址，将第4步中的项目图片保存地址改为你当前blog项目所在的目录地址
![avatar](https://images.gitee.com/uploads/images/2021/0327/121554_3d1df857_5479060.png)  
  
    3.3. 至此所有的准备工作已经就绪，可以尝试启动项目，blog项目是博客系统，包含前端博客展示功能和后端博客管理功能，可单独启动。crawler项目是博客爬虫系统，用于每5分钟对博客网站进行一次爬取，可以单独启动。当你不需要爬取数据的时候，可以不用关注crawler项目，仅仅只把blog项目单独启动作为一个独立的博客系统来学习。
  
4.  项目展示：  
    4.1. 博客系统前台展示，浏览器输入地址localhost:9090
![avatar](https://images.gitee.com/uploads/images/2021/0327/122247_00e68371_5479060.png)  
  
    4.2. 博客系统后台展示，浏览器输入地址localhost:9090/login  登录账号：admin 登录密码：admin123
![avatar](https://images.gitee.com/uploads/images/2021/0327/122258_65c12f37_5479060.png)  
  
    4.3. 爬虫系统爬取数据展示  
![avatar](https://images.gitee.com/uploads/images/2021/0327/122737_2a68009e_5479060.png) 
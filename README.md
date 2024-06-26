<p align="center">
    <a href="" target="_blank">
      <img src="./assets/LOGO.jpg" width="280" />
    </a>
</p>
<h1 align="center">Fast Reserve - GPU预约系统</h1>
<p align="center"><strong>一款面向高校实验室提供的GPU预约系统<br>用于解决高校实验室中GPU资源的使用冲突问题。<em>持续更新中～</em></strong></p>
<div align="center">
    <a href=""><img src="https://img.shields.io/badge/github-项目地址-yellow.svg?style=plasticr"></a>
    <a href=""><img src="https://img.shields.io/badge/前端-项目地址-blueviolet.svg?style=plasticr"></a>
</div>


# FastReserve

## 项目介绍

FastReserve是一款面向高校实验室提供的GPU预约系统，用于解决高校实验室中GPU资源的使用冲突问题。

## 项目结构

```
├─asset                          // 图片
├─backend                        // 后端代码
├─flask                          // Python代码，实现简单GPU监控
├─frontend                       // 前端代码
├─sql                            // 数据库脚本
│       └── reserve_system.sql       // 初始化脚本
├─docker-compose.yml             // 供参考的docker-compose文件
├─Dockfile                       // 用于构建java应用镜像
├─nginx.conf                     // 供参考的简单nginx配置
├─README.md                      // 项目文档
```

## 技术选型

### 前端

| 功能    | 技术   
| ---------- | ------------- 
| 前端核心框架     | [Vue3](https://cn.vuejs.org/)   
| 运行环境     | [Node.js v18.12.1](https://nodejs.org/en)    
| 构建工具     | [Vite](https://vitejs.dev/)  
| 状态管理   | [Pinia](https://pinia.vuejs.org/)                            
| 网络请求 | [Axios](https://axios-http.com/docs/intro)     
| UI框架    | [Element Plus](https://element-plus.org/zh-CN/) 
| UI组件库    | [FullCalendar](https://fullcalendar.io/) 
| 路由管理  | [Vue Router](https://router.vuejs.org/zh/)        
| Markdown渲染  | [Markdown-It](https://github.com/markdown-it/markdown-it)       

### 后端

| 功能    | 技术   
| ---------- | ------------- 
| 开发语言     | [Java8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html) 
| 后端核心框架   | [Spring Boot](https://spring.io/)    
| 构建工具   | [Maven](https://maven.apache.org/)       
| 安全框架 | [Spring Security](https://spring.io/projects/spring-security)     
| ORM框架 | [MyBatis-Plus](https://baomidou.com/)      
| Token | [jjwt](https://github.com/jwtk/jjwt)   
| 关系型数据库 | [MySQL 5.7](https://dev.mysql.com/downloads/mysql/5.7.html)     
| 缓存数据库 | [Redis](https://redis.io/)     
| Api文档 | [Swagger](https://swagger.io/)     

### Python

| 功能    | 技术  
| ---------- | ------------- 
| 开发语言     | [Python3.7](https://www.python.org/downloads/release/python-370/)    
| 部署服务器   | [gunicorn](https://gunicorn.org/)    
| 虚拟环境   | [Anaconda](https://www.anaconda.com/)        
| Web框架 | [Flask](https://flask.palletsprojects.com/)       
| GPU信息获取 | [gpustat](https://github.com/wookayin/gpustat)     

## 演示

首页：
<img src="./assets/demo-1.png" width="500" /> 

预约：
<img src="./assets/demo-2.png" width="500" /> 

工单：
<img src="./assets/demo-3.png" width="500" /> 

审核：
<img src="./assets/demo-4.png" width="500" /> 


## 快速开始

### 前端

```bash
cd frontend 
yarn # 安装依赖
yarn dev # 启动
```

### GPU监控服务部署

> Flask 写的py脚本用于监控GPU的运行情况，每台服务器需单独部署和配置。

前提：
- 服务器需要安装Anaconda环境。
- 将程序上传到自己的服务器单独目录。

首先执行下面命令，创建conda虚拟环境。

```bash
# 安装虚拟环境
conda create --name gpu_monitor_py37 python=3.7
# 启动环境
conda activate gpu_monitor_py37
# 安装依赖
pip install flask
pip install gunicorn
pip install pyyaml
# 部分情况下需要安装gpustat
pip install gpustat
```

配置config.yml：见上面【配置项】部分。

启动脚本：`sh start.sh`

### 后端（本地）

创建数据库：
1. 创建数据库`reserve_system`
2. 导入`sql`目录中的SQL文件

自行启动本地redis

## DockerCompose部署

> 下面教程假设pwd为用户自己新建的项目目录，以目录名gpu为例。

准备：
1. 前端打包文件，名字：dist.zip
2. 后端打包文件，名字：gpu.jar（注意这里的名字和Dockerfile对应）
3. 创建数据卷挂载目录。

首先完成数据卷挂载的准备工作：

```bash
# mysql 挂载准备
mkdir mysql
cd mysql
mkdir data
mkdir init
mkdir conf
cd ..
# nginx 挂载准备
mkdir nginx
cd nginx
mkdir html
# nginx配置文件
vim nginx.conf # 根据需求写配置文件，默认用80端口
# 将dist文件放到nginx/html并解压
cd ..
mv dist.zip nginx/html
cd nginx/html
unzip dist.zip
rm -rf dist.zip
```

部署前，请检查当前目录是否有下面文件和目录，如果没有，请自行通过FTP上传。
- `gpu.jar`(打包后的java程序)
- `mysql`(前面步骤创建的目录)
- `nginx`(前面步骤创建的目录)
- `Dockerfile`
- `docker-compose.yml`

如果没问题，执行下面命令构建并运行容器：
```bash
docker compose up -d
```

检查容器情况，执行下面命令，如果redis、MySQL、Java、nginx四个容器都启动了，说明部署成功。
```bash
docker compose ps
```

接下来是导入数据库数据：
1. 用FTP上传sql脚本。`reserve_system.sql`。
2. 将sql放到容器内。执行：`docker cp reserve_system.sql gpu-mysql:/home`
3. 进入容器运行MYSQL，执行下面命令。

```bash
docker exec -it gpu-mysql mysql -uroot -p # 进入容器
```

```BASH
# 下面是进入MySQL终端后执行的命令
CREATE DATABASE IF NOT EXISTS reserve_system CHARACTER SET utf8mb4
use reserve_system
source /home/reserve_system.sql
```

最后，查看各个容器的日志，观察服务是否正常启动，尤其是Java程序，可以观察有无报错信息。

```bash
docker logs gpu-mysql -f # 查看MySQL运行日志
docker logs gpu-java -f # 查看Java运行日志
docker logs gpu-redis -f # 查看Redis运行日志
docker logs nginx -f # 查看Nginx运行日志
```

到这里，整个应用就部署完毕了，在确认服务器开放了80端口后，可以前往：`http://ip/`，查看网站是否正常运行（如果有域名，应该配置到Nginx中）

## 版本说明

### V1.0

本项目为V1.0版本，于2023年10月开发，也是本人的第一个全栈项目，目前整理过后开源出来。

V1.0前端基于Vue3+ElementPlus+FullCalendar，后端基于Spring Boot+MySQL+Redis，GPU监控服务基于Flask，实现了GPU预约、GPU管理、GPU状态监控、用户管理、基础工单系统等内容。

项目能基本满足高校的大部分需求，如需个性化需求可以自行clone后定制化开发。

### V2.0

> Q：为什么有V2.0？

1.0版本因赶工期开发导致代码并未进行模块化设计，代码不好懂、难维护。因此本人在1.0开发过后又提出了V2.0重构代码的设想。

> Q：V2.0有何不同？

前端方面，V2.0版本计划基于优秀开源框架pure-admin重构，极大提升用户体验。

后端方面，引入如下新特性：
- 基于Sa-Token权限框架，实现Jwt+Redis认证⽅案。
- 基于JSR303标准，使⽤Validation实现数据校验，应⽤过滤器技术防御XSS攻击，提⾼了接⼝安全性
- 基于Spring Event发布订阅模式，结合线程池和异步调⽤技术设计了系统⽇志、登录⽇志和操作⽇志
- 基于AOP和Redission的RateLimiter设计了限流功能，有效防⽌恶意刷请求的情况
- 基于AOP和Redis设计防重提交功能，解决了预约重复提交的问题，确保预约操作的幂等性
- 基于MQ实现风险管控系统，集成邮件通知管理员的功能。
- 设计更好管理各服务器节点GPU监控脚本的系统，包括心跳监测等功能。

> Q：V2.0在哪获取

V2.0暂未发布，开发进度为基本完成后端重构，前端重构还未开始。

众所周知，重构是一件非常麻烦的事情。本人最初也兴致勃勃地设想V2.0并着手开发，可开发到中间发现越来越难以进行下去。本质上在于重构仅仅只是满足我的个人技术爱好，并没有什么实际的需求在推动。此外本人还想花一些时间在学习AI技术上面，因此V2.0暂时的开发计划暂时搁浅。

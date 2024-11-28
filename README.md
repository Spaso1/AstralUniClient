# AstralUniClient 键鼠多操作软件
## 使用场景
- 一套键鼠控制多台电脑
- 显示器在**同一张桌子**上不需要远程桌面即可在一台电脑上操作多台设备
## 解决问题
在一些涉及**渲染,AI**等设备要求均较高的环境下如何提高开发效率和便捷的问题我提供了轻量级的解决方案
## 环境要求:
**Java版本** Oracle,OpenJDK 17+
**服务器环境要求**:
- Window10+ Or Linux(所有版本)
- **客户端环境要求**:
- - Windows10+
- - 至少一套键鼠
- - 支持Direct11+ OpenGL的显示屏
## 搭建
我们客户端和服务端确保足够小,所以将两者合二为一
## 配置文件
在jar文件**同级目录**创建名为`auc`的文件夹,在文件夹里面创建名为`server.prop`的文件*配置文件无论客户端还是服务端都一样*
```A:02-50-AD-1F-2B-57B:00-50-56-C0-00-12SECURE:ASS```
意思是Mac地址为
`02-50-AD-1F-2B-57`
的计算机称为`A`,和服务端的`SECURE`签名为`ASS`Mac地址可以通过直接运行程序获得
```java -jar AstralUniClient.jar```
### 服务端部分
`请先确定配置文件正确`我们的启动非常简单,你只需要配置一些简单的基础参数就可以正常启动```javajava -jar AstralUniClient.jar -server:8000```这里的意思是作为服务端启动,端口是8000
### 客户端部分
`请先确定配置文件正确`客户端分为主客户端和从客户端
```javajava -jar AstralUniClient.jar -client:127.0.0.1:8000 -main:500```
这里的意思是作为**主客户端**启动,端口是8000,服务端ip是127.0.0.1,检测鼠标移动时间为500ms

```javajava -jar AstralUniClient.jar -client:127.0.0.1:8000```这里的意思是作为**从客户端**启动,端口是8000,服务端ip是127.0.0.1

## 切换控制设备
`Ctrl+A`意思是将鼠标和键盘控制切换到代号为A的计算机
`Windows+D`即返回桌面，强制回到本计算机
[github源码和下载](https://github.com/Spaso1/AstralUniClient)
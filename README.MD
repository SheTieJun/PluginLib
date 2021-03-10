## 插件开发

### 插件1：打包上传到蒲公英后发送钉钉 

[uploadPGY](uploadPlugin/README.MD)

### 插件2：加固本地文件，或者打包后加固（地址必须一样）

[360JiaGu](360Plugin/Readme.MD)


### 插件3：邮件插件

[emailPlugin](emailPlugin/ReadMe.md)

### 开发注意事项：
1. 用kotlin 开发的时候 ，注意task 需要使用open
2. 好像无法使用单例创建一些属性
3. 自定Gradle插件的 **3种**用法 
   1. **BuildSrc**，整个`project`都可以使用
   2. `build.gradle`中编写自定义插件,只能当前引用的模块使用
   3. 独立项目，可以上传到`maven`给所有项目使用


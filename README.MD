## 插件开发
[![](https://jitpack.io/v/SheTieJun/PluginLib.svg)](https://jitpack.io/#SheTieJun/PluginLib)

### 插件1：打包上传到蒲公英后发送钉钉 

[uploadPGY](uploadPlugin)
```
classpath com.github.SheTieJun.PluginLib:uploadPlugin:-SNAPSHOT
```
### 插件2：加固本地文件，或者打包后加固

[360JiaGu](360Plugin)
```
classpath com.github.SheTieJun.PluginLib:360Plugin:-SNAPSHOT
```

### 插件3：发送邮件

[emailPlugin](emailPlugin)
```
classpath com.github.SheTieJun.PluginLib:emailPlugin:-SNAPSHOT
```

### 插件4：[AspectJ 集成插件](AspectJPlugin)
```
classpath com.github.SheTieJun.PluginLib:AspectJPlugin:-SNAPSHOT
```


### 开发注意事项：
1. 用kotlin 开发的时候 ，注意task 需要使用open
2. 好像无法使用单例创建一些属性
3. 自定Gradle插件的 **3种**用法 
   1. **BuildSrc**，整个`project`都可以使用
   2. `build.gradle`中编写自定义插件,只能当前引用的模块使用
   3. 独立项目，可以上传到`maven`给所有项目使用


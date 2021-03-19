[360加固帮助](help.MD)
## 思路
/**
 * 1，给我们加固Task设置一个组,这里放到跟上传Task一个组里。
 * 2，创建一个Model类，存放加固时需要的一些参数
 *    比如， 加固jar包文件，账号密码；签名信息；apk加固前，加固后路径等。
 * 3，执行加固时，获取这个Model类的参数信息
 * 4，执行360加固所需要的命令
 * 比如，登陆，导入签名，加固，设置自动签名等等
 */

## 如何使用
1. 项目`build.gradle`
```
    repositories {

        maven{
            maven { url "https://jitpack.io" }
        }
    }

    dependencies {
        classpath 'com.github.SheTieJun.PluginLib:360Plugin:-SNAPSHOT'
    }
```

2. 同时需要在app 下的`build.gradle`
```

```
jiaGuHelper {
       //加固相关的信息
        isAssemble = true //是否重新打包，后进行加固签名
       reinforceJarFile = file("../jiagu/jiagu.jar")
       reinforceUsername = "xxxx"
       reinforcePassword = "xxxx"
       inputApkFile = file("./build/outputs/apk/release/app-release.apk")
       outputFile = file("./channels")
       mulpkgFile = file("./多渠道模板.txt")
   }

```



[自定打包](https://blog.csdn.net/weixin_30693183/article/details/99516981?utm_medium=distribute.pc_relevant.none-task-blog-searchFromBaidu-5.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-searchFromBaidu-5.control)
### 邮件发送插件

https://www.runoob.com/java/java-sending-email.html


1. 项目`build.gradle`
```
    repositories {
        maven{
            maven { url "https://jitpack.io" }
        }
    }

    dependencies {
        classpath 'com.github.SheTieJun.PluginLib:email-plugin:-SNAPSHOT'
    }
```

2. 同时需要在app 下的`build.gradle`
```
 apply plugin: 'shetj.plugin.mail'
 ```
 ```
 EmailHelper {
     emailFrom = "375105540@qq.com"
     emailTo = "3067252268@qq.com"
     emailHost = "smtp.qq.com"
     emailUser = "375105540@qq.com"
     emailPassword = "ccqwqdcnfddvcadd"
     emailMessage = "这是测试"
     emailTitle = "这是标题"
     emailFile = "./多渠道模板.txt"
 }
```

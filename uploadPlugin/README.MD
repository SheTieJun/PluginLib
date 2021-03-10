## 插件：上传到蒲公英

```
    repositories {

        maven{
            url uri('file://F://maven/')
        }
    }

    dependencies {
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1' // Add this line
        classpath 'me.shetj:upload-pgy-plugin:1.0.0'

    }
```

```
maven{
        url 'https://dl.bintray.com/shetiejun/maven'
    }
```

然后下载插件
```
classpath 'me.shetj.plugin:uploadPGY:1.0.0'
```


同时需要在app 下的`build.gradle`

```
apply plugin: 'shetj.plugin.uploadpgy'

uploadPluginHelper {
  uploadPGYHelper {
      key = "fd47xxxxxxxx5923c1f"
      apiKey = "3e2xxxxxxxxxxxx66"
      appName = "test.apk"
      installType = 2
      password = "123"
      //钉钉，这里需要修改成自己的值
      dingWebhook = "https://oapi.dingtalk.com/robot/send?access_token=xx"
      dingKey = "打包机器人"
      title = "新版本APP上传完成(密码:$password)"
      content="体验新版本APP，请到蒲公英下载"
  }

}
```
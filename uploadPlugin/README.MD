## 插件：上传到蒲公英

1. 项目`build.gradle`
```
    repositories {

        maven{
            maven { url "https://jitpack.io" }
        }
    }

    dependencies {
        classpath 'com.github.SheTieJun.PluginLib:upload-pgy-plugin:-SNAPSHOT'
    }
```

2. 同时需要在app 下的`build.gradle`
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
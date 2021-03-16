## AspectJ Gradle 插件

1. 项目`build.gradle`
```
    repositories {
        maven{
            maven { url "https://jitpack.io" }
        }
    }

    dependencies {
        classpath 'com.github.SheTieJun.PluginLib:AspectJPlguin:-SNAPSHOT'
    }
```

2. 同时需要在app 下的`build.gradle`
```
apply plugin: 'shetj.plugin.aspectj'
 ```


3. dependencies 添加
```
implementation 'org.aspectj:aspectjrt:1.9.6'
```

## 待测试
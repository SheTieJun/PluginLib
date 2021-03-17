package com.shetj.uploadplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project

class UploadPlugin : Plugin<Project> {

    companion object {
        val PLUGIN_EXTENSION_NAME = "uploadPGYHelper"
        val ANDROID_EXTENSION_NAME = "android"
    }

    override fun apply(project: Project) {
        val customExtension: UploadExtension = project.extensions.create(
            PLUGIN_EXTENSION_NAME,
            UploadExtension::class.java
        )

        if (customExtension.apiKey == null) {
            return
        }
        //项目编译完成后，回调
        project.afterEvaluate {
            val appVariants: DomainObjectSet<ApplicationVariant> = (project
                .extensions
                .findByName(ANDROID_EXTENSION_NAME) as AppExtension?)!!.applicationVariants
            //release apk
            println("start project UploadPlugin")
            //这里，可以实现多渠道的打包
            for (variant in appVariants) {
                //只监听buildType是uploadRelease的包
                if (variant.buildType.name.equals("release", true)) {
                    val variantName: String =
                        variant.name.substring(0, 1).toUpperCase() + variant.name
                            .substring(1)

                    /**
                    创建我们，上传到蒲公英的task任务
                     */
                    val uploadTask: UploadTask = project.tasks
                        .create("uploadPGYWith$variantName", UploadTask::class.java)
                    uploadTask.init(variant, project)

                    variant.assembleProvider.get().dependsOn(project.tasks.findByName("clean"))
                    uploadTask.dependsOn(variant.assembleProvider.get())

                    //依赖关系 。上传依赖打包，打包依赖clean。
                    if (customExtension.dingWebhook != null) {
                        //发送信息到webHook
                        val talkTask = project.tasks
                            .create(
                                "UploadPGYAndDingTalkWith$variantName",
                                SendMessageTask::class.java
                            )
                        talkTask.init(variant, project)
                        talkTask.dependsOn(uploadTask);
                    }
                    break
                }
            }
        }
    }
}
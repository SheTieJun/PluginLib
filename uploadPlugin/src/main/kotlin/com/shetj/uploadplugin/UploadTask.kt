package com.shetj.uploadplugin

import com.android.build.gradle.api.BaseVariant
import com.google.gson.Gson
import com.shetj.uploadplugin.UploadExtension.Companion.getConfig
import com.shetj.utils.RetrofitUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File


open class UploadTask : DefaultTask() {

    private var mVariant: BaseVariant? = null
    private var mTargetProject: Project? = null


    data class RequestInfo(
        val key: String?,
        val apiKey: String?,
        val appName: String?,
        val installType: Int = 1, //   //1,install by public 2,install by password 3,install by invite
        val password: String?
    )

    fun init(variant: BaseVariant?, project: Project?) {
        mVariant = variant
        mTargetProject = project
        description = "upload to pgy"
        group = UploadPlugin.ANDROID_EXTENSION_NAME
    }

    @TaskAction
    fun startUpload() {
        println("start project startUpload")
        if (mVariant != null) {
            for (output in mVariant!!.outputs) {
                val file: File = output.outputFile
                if (!file.exists()) {
                    throw GradleException("apk file is not exist!")
                }
                val extension =
                    mTargetProject?.let { getConfig(it) }
                extension?.let {
                    val request = RequestInfo(
                        extension.key,
                        extension.apiKey,
                        extension.appName,
                        extension.installType,
                        extension.password
                    )
                    upload(request, file)
                }
            }
        }

    }

    private fun upload(requestInfo: RequestInfo, apkFile: File) {

        println("start upload")

        val fileRQ = RequestBody.create(MediaType.parse("*/*"), apkFile);
        val part = MultipartBody.Part.createFormData(
            "file",
            requestInfo.appName?.let {
                if (it.endsWith(".apk", ignoreCase = true)) {
                    requestInfo.appName
                } else {
                    println("request Info.appName must end With .apk.")
                    apkFile.name
                }
            } ?: apkFile.name,
            fileRQ
        )
        try {
            val mApiKey = RequestBody.create(MediaType.parse("text/plain"), requestInfo.apiKey!!)
            val mKey = RequestBody.create(MediaType.parse("text/plain"), requestInfo.key!!)
            val buildInstallType = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                requestInfo.installType.toString()
            )

            val buildPassword =
                requestInfo.password?.let { RequestBody.create(MediaType.parse("text/plain"), it) }
            val body = RetrofitUtils.instance.create(UploadApiService::class.java)
                .uploadFile(mKey, mApiKey, buildInstallType, buildPassword, part)
                .execute().body()
            //必须执行这一句，不可以省略
            val string = body?.string()
            println("upload result = $string")
            parseResult(string)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun parseResult(result: String?) {
        if (result == null) {
            println("upload apk to PGY failed")
            return
        }
        val gson = Gson()
        try {
            val data: PGYResonpse = gson.fromJson(result, PGYResonpse::class.java)
            if (data.code != 0) {
                println("upload apk to PGY failed")
                return
            }
            SendMessageTask.setData(data.data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}
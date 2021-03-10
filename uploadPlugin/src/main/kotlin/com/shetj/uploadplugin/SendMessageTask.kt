package com.shetj.uploadplugin

import com.android.build.gradle.api.BaseVariant
import com.google.gson.Gson
import com.shetj.uploadplugin.UploadPlugin.Companion.ANDROID_EXTENSION_NAME
import com.shetj.utils.RetrofitUtils
import okhttp3.FormBody
import okhttp3.MediaType
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

open class SendMessageTask : DefaultTask() {
    private var mVariant: BaseVariant? = null
    private var mTargetProject: Project? = null
    private var mWebhook: String? = null
    private var mSecret: String? = null

    class DingTalkLinkModel {
        var picUrl: String? = null
        var messageUrl: String? = null
        var title: String? = null
        var text: String? = null
    }

    data class DingTalkRequest(var link: DingTalkLinkModel, var msgtype: String = "link")

    fun init(
        variant: BaseVariant?,
        project: Project?
    ) {
        mVariant = variant
        mTargetProject = project
        description = "send message to ding talk"
        group = ANDROID_EXTENSION_NAME
    }

    @TaskAction
    fun sendDingtalk() {
        val model = initModel()
        val url = createDingUrl()
        val gson = Gson()
        val json = gson.toJson(DingTalkRequest(model))
        val requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        val body = RetrofitUtils.instance.create(UploadApiService::class.java)
            .sendToDding(url, requestBody)
            .execute().body()
        println("upload = " + body?.string())
    }

    private fun createDingUrl(): String {
        return mWebhook!!
    }

    private fun initModel(): DingTalkLinkModel {
        val model = DingTalkLinkModel()
        model.picUrl = mPGYResonpse?.buildQRCodeURL
        model.messageUrl = "http://www.pgyer.com/${mPGYResonpse?.buildShortcutUrl}"
        val extension: UploadExtension =
            UploadExtension.getConfig(mTargetProject!!)
        model.title =extension.dingKey + ":"+ extension.title
        model.text = extension.content
        mWebhook = extension.dingWebhook
        return model
    }


    companion object {
        private var mPGYResonpse: PGYInfo? = null
        fun setData(body: PGYInfo?) {
            mPGYResonpse = body
        }
    }
}
package com.shetj.uploadplugin

import org.gradle.api.Project


open class UploadExtension(
    var key: String? = null,
    var apiKey: String? = null,
    var installType: Int = 1,
    var password: String?=null,
    var appName: String? = null,
    var dingWebhook: String? = null,
    var dingKey: String? = null,
    var title: String? = null,
    var content: String? = null
) {

    companion object{

        fun getConfig(project: Project): UploadExtension {
            var extension: UploadExtension? =
                project.extensions.findByType(UploadExtension::class.java)
            if (extension == null) {
                extension = UploadExtension()
            }
            return extension
        }
    }
}
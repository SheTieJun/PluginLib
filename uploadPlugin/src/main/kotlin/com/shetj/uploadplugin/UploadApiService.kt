package com.shetj.uploadplugin

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface UploadApiService {

    /**
     * https://www.pgyer.com/doc/view/api#uploadApp
     */
    @Multipart
    @POST("https://www.pgyer.com/apiv2/app/upload")
    fun uploadFile(
        @Part("ukey") ukey: RequestBody,
        @Part("_api_key") apiKey: RequestBody,
        @Part("buildInstallType") installType: RequestBody,
        @Part("buildPassword") password: RequestBody?,
        @Part file: MultipartBody.Part?
    ):Call<ResponseBody>

//    @Multipart
//    @POST("https://www.pgyer.com/apiv2/app/upload")
//    fun uploadFile(@Part file: MultipartBody.Part?,@PartMap maps: Map<String, RequestBody>):Call<ResponseBody>


    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun sendToDding(@Url url: String?,@Body jsonBody: RequestBody?):Call<ResponseBody>

}
package com.example.oneplusone.serverConnection

import com.example.oneplusone.model.data.ProductData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    //처음 실행시 마지막 접속 시간을 서버에 전송하고 서버에서 업데이트 정보의 여부에 따라 답장을 받음
    //코루틴을 사용해서 서버와 통신할때는 enqueue를사용할 필요가 없기 때문에 Call 대신 Response를 쓴다.
    @POST("/updateInfoCheck")
    suspend fun updateInfoCheck(@Body lastConnectTime:Int?): Response<Int?>

    //상품 정보들을 가져옴
    @GET("product/GS25")
    suspend fun getGS25ProductData(): Call<List<ProductData>>

    @GET("product/CU")
    suspend fun getCUProductData(): Call<List<ProductData>>

    @GET("product/SevenEleven")
    suspend fun getSevenElevenProductData(): Call<List<ProductData>>

    @GET("product/Emart24")
    suspend fun getEmart24ProductData(): Call<List<ProductData>>

    //상품의 검색 순위를 가져옴
    @GET("productRanking")
    suspend fun getProductRanking(): Call<List<String>>
}
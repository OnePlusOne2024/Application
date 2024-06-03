package com.example.oneplusone.serverConnection

import com.example.oneplusone.db.ProductData
import com.example.oneplusone.model.data.ProductRanking
import com.example.oneplusone.model.data.ServerConvenienceResult
import com.example.oneplusone.model.data.ServerProductSearchRankingResult
import com.example.oneplusone.model.data.ServerResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime

interface API {

    //처음 실행시 마지막 접속 시간을 서버에 전송하고 서버에서 업데이트 정보의 여부에 따라 답장을 받음
    //코루틴을 사용해서 서버와 통신할때는 enqueue를사용할 필요가 없기 때문에 Call 대신 Response를 쓴다.
    @POST("/updateInfoCheck")
    suspend fun updateInfoCheck(@Body lastConnectTime:String?): Response<Boolean>

    @GET("/api/v1/product/read_all")
    suspend fun getProductList(@Query("clientTime")clientTime: String?): Response<ServerResponse>

    @GET("/api/v1/convenience/near_conv")
    suspend fun getConvenienceData(
        @Query("latitude")latitude: String?,
        @Query("longitude")longitude: String?,
    ): Response<ServerConvenienceResult>


    @POST("/api/v1/product/search")
    suspend fun postSearchText(@Query("productName")productName: String):Response<ResponseBody>

    @GET("/api/v1/product/topSearched")
    suspend fun getProductSearchRanking(): Response<List<ProductRanking>>

    //상품 정보들을 가져옴//하나로 통일
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
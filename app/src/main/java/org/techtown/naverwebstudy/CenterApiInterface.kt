package org.techtown.naverwebstudy

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CenterApiInterface {
    @GET("15077586/v1/centers")
    fun getCenter(@Query("page")page:Int ,
                  @Query("perPage")perPage:Int ,
                  @Query("returnType")returnType:String):Call<DataResult>
    //호출 파라미터 지정해야함 -> api 목록에 나와있음
}
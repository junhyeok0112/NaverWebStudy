package org.techtown.naverwebstudy

//Json 형태 받을 데이터 클래스 정의
data class DataResult (
    var page:Int = 0,
    var perPage:Int = 0,
    var totalCount:Int = 0,
    var currentCount:Int = 0,
    var data: List<data>
)

data class data(
    var id:Int = 0,
    var centerName: String ="",
    var sido: String ="",
    var sigungu: String ="",
    var facilityName: String ="",
    var zipCode: String ="",
    var address: String ="",
    var lat: String ="",
    var lng: String ="",
    var createdAt: String ="",
    var updatedAt: String ="",
    var centerType: String ="",
    var org: String ="",
    var phoneNumber: String ="",
)


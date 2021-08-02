package org.techtown.naverwebstudy

import android.content.ContentValues.TAG
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import org.techtown.naverwebstudy.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() ,OnMapReadyCallback {

    lateinit var binding: ActivityMainBinding
    lateinit var locationSource: FusedLocationSource
    lateinit var naverMap: NaverMap
    val key = "OdlxmLGpJcnLkvXMDMYcwrmWf6By1UPn4dw0Z5oX8KKc1ymyFtL8L/jwE2JqRJpRU06qE552ZpFv7DW3JC31Vw=="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //현재 위치에 접근 할 수 있게 FusedLocationSource 사용
        //네이버 지도에서 이거 이용할 수 있게 해줘야함
        binding.map.getMapAsync(this)       //getMapAsync 하면 Navermap 객체 얻고 onMapReady 호출

    }

    override fun onMapReady(naverMap: NaverMap) {                 //마크 설정하려면 OnMapReadyCallback을 상속받고 여기서 설정
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        //locationSource가 현재 위치 탐색한 뒤 네이버 지도에 위치 알려줌
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        //지도상에 location 버튼 활성화 -> 네이버 지도의 UI 셋팅에 대한 객체 얻어와서 진행
        //누르면 내 위치로 이동하는 버튼 추가됨
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true           //자바에서는 uiSettings.setLocationButtonEnabled(true);

        callRetrofit()
        //var mapCenter:LatLng = naverMap.cameraPosition.target   //카메라상 보이는 곳의 중심 위도,경도 얻을 수 있음


    }

    // 네이버 지도 현재 위치에 접근하기 위해서 Permission 설정
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    fun callRetrofit(){     //retrofit 호출 함수
        Log.d("진입" , "callRetrofit 진입")
        val retrofit = Retrofit.Builder()                               //retrofit 객체 생성
            .baseUrl("https://api.odcloud.kr/api/")
            .addConverterFactory(GsonConverterFactory.create())         //json 객체를 java객체로 변경하기 위한 컨버터
            .build()
        var centerApi:CenterApiInterface? = retrofit.create(CenterApiInterface::class.java) //retrofit 객체 사용해서 interface갖는 객체생성
        centerApi?.getCenter(1,50,key)?.enqueue(object :Callback<CenterData>{
            //파라미터 전달하고 결과는 Callback으로 받음 -> 우리가 정의한 거
            override fun onResponse(call: Call<CenterData>, response: Response<CenterData>) {
                //response로 요청한 데이터에 접근가능 response?.body()?.* 의 형식으로
                Log.d("성공","성공 : ${response.body()?.data?.size}")
                var result = response.body()
                updateMarker(result)
            }

            override fun onFailure(call: Call<CenterData>, t: Throwable) {
                Log.d("실패" , " 실패")
            }
        })
    }

    fun updateMarker(result: CenterData?){
        //resetMarkerList()
        Log.d("진입" , "실행")
        if(result?.data == null) {
            Log.d("실패" , "실행")
        }
        if(result?.data != null && result?.data.size>0){
            Log.d("성공" , "실행")
            for(data:Items in result.data) {
                val marker = Marker()
                marker.position = LatLng(data.lat.toDouble(), data.lng.toDouble())
                marker.icon = OverlayImage.fromResource(R.drawable.marker_red)
                marker.anchor = PointF(0.5f ,1.0f)  //anchor 속성을 지정하면 이미지가 가리키는 지점과 마커가 위치한 지점을 일치시킬 수 있습니다.
                //아이콘에서 앵커로 지정된 지점이 마커의 좌표에 위치하게 됩니다.
                marker.map = naverMap

            }

        }
    }
}
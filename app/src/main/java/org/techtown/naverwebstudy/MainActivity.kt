package org.techtown.naverwebstudy

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
        var mapCenter:LatLng = naverMap.cameraPosition.target   //카메라상 보이는 곳의 중심 위도,경도 얻을 수 있음


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
        val retrofit = Retrofit.Builder()                               //retrofit 객체 생성
            .baseUrl("https://api.odcloud.kr/api/")
            .addConverterFactory(GsonConverterFactory.create())         //json 객체를 java객체로 변경하기 위한 컨버터
            .build()
        var centerApi:CenterApiInterface = retrofit.create(CenterApiInterface::class.java) //retrofit 객체 사용해서 interface갖는 객체생성
        centerApi?.getCenter(1,50,"JSON")?.enqueue(object :Callback<DataResult>{
            //파라미터 전달하고 결과는 Callback으로 받음 -> 우리가 정의한 거
            override fun onResponse(call: Call<DataResult>, response: Response<DataResult>) {
                //response로 요청한 데이터에 접근가능 response?.body()?.* 의 형식으로
                var result = response.body()
                updateMarker(result)
            }

            override fun onFailure(call: Call<DataResult>, t: Throwable) {
                Log.d("실패" , " 실패")
            }
        })
    }

    fun updateMarker(result: DataResult?){
        resetMarkerList()
    }

    fun resetMarkerList(){

    }
}
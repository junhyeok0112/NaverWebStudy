package org.techtown.naverwebstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import org.techtown.naverwebstudy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() ,OnMapReadyCallback {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.map.getMapAsync(this)       //getMapAsync 하면 Navermap 객체 얻고 onMapReady 호출

    }

    override fun onMapReady(naverMap: NaverMap) {                 //마크 설정하려면 OnMapReadyCallback을 상속받고 여기서 설정
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = naverMap

        marker.setOnClickListener {o->      //리턴으로 boolean 타입
            Toast.makeText(this.applicationContext , "마커 클릭" , Toast.LENGTH_SHORT).show()
            true
        }
    }

}
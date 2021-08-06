package com.summer.project

import android.content.ContentUris
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.spin_profile_city
import kotlinx.android.synthetic.main.activity_profile.spin_profile_innercity
import kotlinx.android.synthetic.main.temporary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

//클래스 만들기

class ProfileActivity : AppCompatActivity() {
    private  lateinit var server: Service
    lateinit var storage: FirebaseStorage
    var photoUri : Uri? = null
    var address : Uri? = null
    var one = 0
    var two = 0
    var token : String? = null
    val city = arrayOf("지역","울산광역시", "충청남도", "서울특별시", "세종특별자치시", "전라북도", "경기도", "광주광역시", "인천광역시", "전라남도", "강원도", "충청북도", "대구광역시", "경상남도", "경상북도", "부산광역시", "제주특별자치도", "대전광역시")
    val innercity = arrayOf(arrayOf("세부 지역")
        ,arrayOf("동구", "울주군", "남구", "북구", "중구")
        ,arrayOf("청양군", "천안시동남구", "천안시", "태안군", "부여군", "예산군", "논산시", "서산시", "보령시", "금산군", "홍성군", "당진시", "아산시", "서천군", "계룡시", "공주시", "천안시서북구")
        ,arrayOf("중랑구", "서대문구", "구로구", "중구", "서초구", "강북구", "용산구", "도봉구", "노원구", "영등포구", "강동구", "성북구", "은평구", "광진구", "마포구", "동작구", "동대문구", "양천구", "강남구", "관악구", "송파구", "금천구", "종로구", "강서구", "성동구")
        ,arrayOf("세종시")
        ,arrayOf("김제시", "장수군", "순창군", "부안군", "무주군", "군산시", "남원시", "정읍시", "전주시덕진구", "진안군", "고창군", "전주시완산구", "완주군", "전주시", "익산시", "임실군")
        ,arrayOf("김포시", "안산시상록구", "동두천시", "오산시", "용인시", "성남시", "안양시", "용인시기흥구", "안양시동안구", "화성시", "광주시", "의왕시", "포천시", "수원시팔달구", "평택시", "수원시", "안산시", "양평군", "과천시", "고양시덕양구", "성남시수정구", "의정부시", "수원시권선구", "파주시", "양주시", "광명시", "가평군", "여주시", "안양시만안구", "연천군", "부천시", "하남시", "군포시", "수원시장안구", "안산시단원구", "용인시처인구", "고양시일산동구", "시흥시", "성남시중원구", "성남시분당구", "안성시", "고양시", "남양주시", "수원시영통구", "구리시", "이천시", "고양시일산서구", "용인시수지구")
        ,arrayOf("동구", "북구", "남구", "광산구", "서구")
        ,arrayOf("미추홀구", "강화군", "동구", "남동구", "부평구", "옹진군", "서구", "계양구", "연수구", "중구")
        ,arrayOf("광양시", "목포시", "장성군", "함평군", "화순군", "담양군", "무안군", "여수시", "해남군", "나주시", "영암군", "완도군", "순천시", "진도군", "고흥군", "보성군", "곡성군", "장흥군", "강진군", "영광군", "구례군", "신안군")
        ,arrayOf("횡성군", "삼척시", "인제군", "속초시", "동해시", "정선군", "평창군", "홍천군", "영월군", "춘천시", "원주시", "양구군", "강릉시", "화천군", "양양군", "태백시", "철원군", "고성군")
        ,arrayOf("청주시", "단양군", "괴산군", "진천군", "제천시", "영동군", "보은군", "청주시상당구", "음성군", "증평군", "청주시흥덕구", "청주시청원구", "충주시", "청주시서원구", "옥천군")
        ,arrayOf("달서구", "동구", "달성군", "북구", "수성구", "남구", "서구", "중구")
        ,arrayOf("거창군", "거제시", "사천시", "창원시성산구", "밀양시", "창원시마산회원구", "산청군", "양산시", "창녕군", "합천군", "의령군", "남해군", "창원시", "김해시", "창원시의창구", "창원시마산합포구", "함양군", "하동군", "통영시", "진주시", "고성군", "창원시진해구", "함안군")
        ,arrayOf("군위군", "상주시", "영양군", "청도군", "포항시북구", "영주시", "문경시", "영덕군", "김천시", "성주군", "경주시", "구미시", "포항시", "칠곡", "고령군", "포항시남구", "청송군", "영천시", "울진군","안동시", "의성군", "경산시", "울릉군", "예천군", "봉화군")
        ,arrayOf("금정구", "기장군", "수영구", "동구", "부산진구", "해운대구", "동래구", "연제구", "사상구", "북구", "강서구", "남구", "사하구", "서구", "영도구", "중구")
        ,arrayOf("제주시", "서귀포시")
        ,arrayOf("대덕구", "동구", "유성구", "서구", "중구"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        storage = FirebaseStorage.getInstance()

        val intent = intent
        token = intent.getStringExtra("token")

        val retrofit = Retrofit.Builder()       //retrofit(Http 연동 - Rest API)
            .baseUrl("http://10.0.2.2:8001/")   //android 로컬 주소(localhost 느낌)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        server = retrofit.create(Service::class.java)

        val adapter : ArrayAdapter<String>
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,city)
        spin_profile_city.adapter = adapter

        spin_profile_innercity.isEnabled = false

        spin_profile_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val next_adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, innercity[p2])
                spin_profile_innercity.adapter = next_adapter
                spin_profile_innercity.isEnabled = true
                one = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        spin_profile_innercity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                two = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }



        btn_profile_imgChange.setOnClickListener {
            val photoPickIntent = Intent(Intent.ACTION_PICK)
            photoPickIntent.type = "image/*"
            startActivityForResult(photoPickIntent,0)
        }

        btn_profile_previous.visibility = View.INVISIBLE

        btn_profile_next.setOnClickListener {
            if(vf_profile_profile.currentView==layout_proflie_basicinfo){
                btn_profile_previous.visibility = View.VISIBLE
                btn_profile_next.text = "완료"
                vf_profile_profile.showNext()

            } else{
                if(photoUri == null){
                    Toast.makeText(this,"이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (one == 0){
                    Toast.makeText(this,"지역을 선택해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if(edt_profile_nickname.isEnabled == true){
                    Toast.makeText(this, "닉네임 중복을 확인해주세요", Toast.LENGTH_SHORT).show()
                } else{
                    if (token != null) {
                        imgUpload()

                    }
                }
            }
        }

        btn_profile_previous.setOnClickListener {
            vf_profile_profile.showPrevious()
            btn_profile_next.text = "다음"
            btn_profile_previous.visibility = View.INVISIBLE
        }

        btn_profile_doublecheck.setOnClickListener {
            if(btn_profile_doublecheck.text.toString() == "다시입력"){
                edt_profile_nickname.isEnabled = true
                btn_profile_doublecheck.text = "중복확인"
                return@setOnClickListener
            }
            if(edt_profile_nickname.text.toString() == null){
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else{
                server.NicknameDoubleCheck(edt_profile_nickname.text.toString()).enqueue(object : Callback<ResponseMSG>{
                    override fun onResponse(
                        call: Call<ResponseMSG>,
                        response: Response<ResponseMSG>
                    ) {
                        if(response.body()?.message.toString() == "good"){
                            Toast.makeText(this@ProfileActivity,"사용가능합니다", Toast.LENGTH_SHORT).show()
                            btn_profile_doublecheck.text = "다시입력"
                            edt_profile_nickname.isEnabled = false
                        } else{
                            Toast.makeText(this@ProfileActivity, "아이디가 중복됩니다", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseMSG>, t: Throwable) {

                    }

                })
            }
        }


    }

    private fun imgUpload(){
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_"+timestamp+"_png"

        var storageRef = storage?.reference?.child("Users")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri->
                address = uri
                server.RegisterProfile(token!!,address.toString(),edt_profile_nickname.text.toString(), "${city[one]} ${innercity[one][two]}").enqueue(object: Callback<ResponseDTO>{
                    override fun onResponse(
                        call: Call<ResponseDTO>,
                        response: Response<ResponseDTO>
                    ) {
                        Toast.makeText(this@ProfileActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {

                    }

                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            photoUri = data?.data
            if(photoUri != null){
                img_profile_img.setImageURI(photoUri)
            }
        } else{
            finish()
        }
    }
}
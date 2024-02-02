package com.example.guru_callytime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {
    private lateinit var btnMyInfo: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        btnMyInfo = findViewById(R.id.btnMyInfo)
        btnLogout = findViewById(R.id.btnLogout)

        btnMyInfo.setOnClickListener {
            // 내 정보 페이지로 이동
            val intent = Intent(this, MyInfoActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // 로그아웃 -> 앱 시작화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
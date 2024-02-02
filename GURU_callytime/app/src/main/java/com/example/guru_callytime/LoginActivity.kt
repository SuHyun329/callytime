package com.example.guru_callytime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextUserId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonSignUp: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 뷰 초기화
        editTextUserId = findViewById(R.id.editTextUserId)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        dbHelper = DBHelper(this)

        // 로그인 버튼 클릭 이벤트
        buttonLogin.setOnClickListener {
            val userId = editTextUserId.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "ID와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // DBHelper를 사용하여 사용자가 존재하는지 확인
                if (dbHelper.checkUser(userId, password)) {
                    // 로그인 성공 시 메인 액티비티로 이동 (또는 다른 화면)
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // val intent = Intent(this, MainActivity::class.java)
                    // startActivity(intent)
                } else {
                    // 로그인 실패 시 오류 메시지 표시
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 회원가입 버튼 클릭 이벤트
        buttonSignUp.setOnClickListener {
            // SignUpActivity로 이동
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}

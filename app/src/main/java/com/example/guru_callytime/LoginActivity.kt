package com.example.guru_callytime // 패키지 정의

import android.content.Intent // 인텐트를 사용하기 위한 임포트
import android.os.Bundle // 안드로이드 OS 번들 클래스 임포트
import android.widget.Button // 버튼 위젯 임포트
import android.widget.EditText // 에디트텍스트 위젯 임포트
import android.widget.Toast // 토스트 메시지 위젯 임포트
import androidx.appcompat.app.AppCompatActivity // 앱 호환성을 위한 액티비티 클래스 임포트

// 로그인 화면을 위한 액티비티 클래스
class LoginActivity : AppCompatActivity() {
    // 뷰 요소 선언
    private lateinit var editTextUserId: EditText // 사용자 ID 입력 필드
    private lateinit var editTextPassword: EditText // 비밀번호 입력 필드
    private lateinit var buttonLogin: Button // 로그인 버튼
    private lateinit var buttonSignUp: Button // 회원가입 버튼
    private lateinit var dbHelper: DBHelper // 데이터베이스 헬퍼 객체

    // 액티비티 생성 시 호출되는 함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // 레이아웃 설정

        // 뷰 요소와 레이아웃 ID 연결
        editTextUserId = findViewById(R.id.editTextUserId)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        dbHelper = DBHelper(this) // DBHelper 인스턴스 생성

        // 로그인 버튼 클릭 이벤트 리스너 설정
        buttonLogin.setOnClickListener {
            val userId = editTextUserId.text.toString().trim() // 사용자 ID 입력 값 가져오기
            val password = editTextPassword.text.toString().trim() // 비밀번호 입력 값 가져오기

            // 입력 필드의 값이 비어있는지 확인
            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "ID와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // DBHelper를 사용하여 사용자가 존재하는지 확인
                if (dbHelper.checkUser(userId, password)) {
                    // 로그인 성공 시 메인 액티비티로 이동 (또는 다른 화면)
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity_navi::class.java)
                    startActivity(intent) // 주석 해제 시 메인 액티비티로 이동
                } else {
                    // 로그인 실패 시 오류 메시지 표시
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 회원가입 버튼 클릭 이벤트 리스너 설정
        buttonSignUp.setOnClickListener {
            // SignUpActivity로 이동
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent) // 인텐트 시작
        }
    }
}


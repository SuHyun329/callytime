package com.example.guru_callytime // 패키지 정의

import android.os.Bundle // 필요한 안드로이드 클래스 임포트
import android.widget.Button // 버튼 위젯 임포트
import android.widget.EditText // 에디트텍스트 위젯 임포트
import android.widget.Toast // 토스트 메시지 위젯 임포트
import androidx.appcompat.app.AppCompatActivity // 앱 호환성을 위한 액티비티 클래스 임포트

// 회원가입을 위한 액티비티 클래스 정의
class SignUpActivity : AppCompatActivity() {
    // 뷰 요소 선언
    private lateinit var editTextName: EditText // 이름 입력 필드
    private lateinit var editTextBirthdate: EditText // 생년월일 입력 필드
    private lateinit var editTextUserId: EditText // 사용자 ID 입력 필드
    private lateinit var editTextPassword: EditText // 비밀번호 입력 필드
    private lateinit var editTextEmail: EditText // 이메일 입력 필드
    private lateinit var buttonSignUp: Button // 회원가입 버튼
    private lateinit var dbHelper: DBHelper // 데이터베이스 헬퍼 객체

    // 액티비티 생성 시 호출되는 함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up) // 레이아웃 설정

        // 뷰 요소와 레이아웃 ID 연결
        editTextName = findViewById(R.id.editTextUsername)
        editTextBirthdate = findViewById(R.id.birthEditText)
        editTextUserId = findViewById(R.id.editTextUserId)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        dbHelper = DBHelper(this) // DBHelper 인스턴스 생성

        // '등록' 버튼 클릭 이벤트 리스너 설정
        buttonSignUp.setOnClickListener {
            val name = editTextName.text.toString().trim() // 이름 입력 값 가져오기
            val birthdate = editTextBirthdate.text.toString().trim() // 생년월일 입력 값 가져오기
            val userId = editTextUserId.text.toString().trim() // 사용자 ID 입력 값 가져오기
            val password = editTextPassword.text.toString().trim() // 비밀번호 입력 값 가져오기
            val email = editTextEmail.text.toString().trim() // 이메일 입력 값 가져오기

            // 입력 필드의 값이 비어있는지 확인
            if (name.isEmpty() || birthdate.isEmpty() || userId.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // DBHelper를 사용하여 사용자 정보 데이터베이스에 추가
                val addUserResult = dbHelper.addUser(name, birthdate, userId, password, email)
                if (addUserResult) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    finish()  // 회원가입 성공 후 화면 종료
                } else {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


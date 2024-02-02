package com.example.guru_callytime

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextBirthdate: EditText
    private lateinit var editTextUserId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // 뷰 연결
        editTextName = findViewById(R.id.editTextUsername)
        editTextBirthdate = findViewById(R.id.birthEditText)
        editTextUserId = findViewById(R.id.editTextUserId)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        dbHelper = DBHelper(this)

        // '등록' 버튼 클릭 이벤트 리스너
        buttonSignUp.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val birthdate = editTextBirthdate.text.toString().trim()
            val userId = editTextUserId.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val email = editTextEmail.text.toString().trim()

            if (name.isEmpty() || birthdate.isEmpty() || userId.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 사용자 정보 데이터베이스에 추가
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

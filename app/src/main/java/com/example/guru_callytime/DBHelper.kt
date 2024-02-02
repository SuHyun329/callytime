package com.example.guru_callytime // 패키지 정의

import android.content.ContentValues // ContentValues 클래스 가져오기
import android.content.Context // Context 클래스 가져오기
import android.database.sqlite.SQLiteDatabase // SQLiteDatabase 클래스 가져오기
import android.database.sqlite.SQLiteOpenHelper // SQLiteOpenHelper 클래스 가져오기

// DBHelper 클래스, SQLiteOpenHelper 상속, SQLite 데이터베이스 관리
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // 데이터베이스 설정 관련 상수 저장
    companion object {
        private const val DATABASE_NAME = "Sign_UserDB.db" // 데이터베이스 이름
        private const val DATABASE_VERSION = 1 // 데이터베이스 버전
        private const val TABLE_USERS = "users" // 사용자 테이블 이름
        private const val COLUMN_ID = "id" // 아이디 컬럼
        private const val COLUMN_NAME = "name" // 이름 컬럼
        private const val COLUMN_BIRTHDATE = "birthdate" // 생년월일 컬럼
        private const val COLUMN_USER_ID = "user_id" // 사용자 아이디 컬럼
        private const val COLUMN_PASSWORD = "password" // 비밀번호 컬럼
        private const val COLUMN_EMAIL = "email" // 이메일 컬럼
    }

    // 데이터베이스 생성 시 호출, 사용자 테이블 생성
    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_BIRTHDATE TEXT,
                $COLUMN_USER_ID TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_EMAIL TEXT
            )
        """.trimIndent()
        db.execSQL(createTableSQL) // 테이블 생성 쿼리 실행
    }

    // 데이터베이스 업그레이드 시 호출, 기존 테이블 삭제 후 재생성
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS") // 기존 테이블 삭제 쿼리 실행
        onCreate(db) // 테이블 재생성
    }

    // 사용자 정보를 데이터베이스에 추가하는 함수
    fun addUser(name: String, birthdate: String, userId: String, password: String, email: String): Boolean {
        val db = this.writableDatabase // 쓰기 가능한 데이터베이스 인스턴스 가져오기
        val values = ContentValues().apply {
            put(COLUMN_NAME, name) // 이름 값 설정
            put(COLUMN_BIRTHDATE, birthdate) // 생년월일 값 설정
            put(COLUMN_USER_ID, userId) // 사용자 아이디 값 설정
            put(COLUMN_PASSWORD, password) // 비밀번호 값 설정
            put(COLUMN_EMAIL, email) // 이메일 값 설정
        }
        val result = db.insert(TABLE_USERS, null, values) // 데이터베이스에 새로운 사용자 정보 삽입
        db.close() // 데이터베이스 연결 닫기
        return result != -1L // 삽입 성공 시 true 반환, 실패 시 false 반환
    }

    // 사용자 확인 함수, 존재 시 true 반환
    fun checkUser(userId: String, password: String): Boolean {
        val db = this.readableDatabase // 읽기 가능한 데이터베이스 인스턴스 가져오기
        val cursor = db.query(
                TABLE_USERS, // 조회할 테이블 지정
                arrayOf(COLUMN_ID), // 가져올 컬럼 지정, 여기서는 ID만 가져옴
                "$COLUMN_USER_ID = ? AND $COLUMN_PASSWORD = ?", // 조건 지정: 사용자 ID와 비밀번호가 일치하는지 확인
                arrayOf(userId, password), // 조건에 사용될 인자 제공
                null, // groupBy 인자, 여기서는 사용하지 않으므로 null
                null, // having 인자, 여기서는 사용하지 않으므로 null
                null  // orderBy 인자, 여기서는 사용하지 않으므로 null
        )
        val userExists = cursor.moveToFirst() // 첫 번째 행이 존재하는지 확인 (사용자 존재 여부)
        cursor.close() // 커서 닫기
        db.close() // 데이터베이스 연결 닫기
        return userExists // 사용자 존재 여부 반환
    }

}


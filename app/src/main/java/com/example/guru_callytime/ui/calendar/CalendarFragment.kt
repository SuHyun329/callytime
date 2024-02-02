package com.example.guru_callytime.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guru_callytime.R
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class CalendarFragment : Fragment() {
    private lateinit var calendarViewModel: CalendarViewModel
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var calendarView: CalendarView
    lateinit var updateBtn: Button
    lateinit var deleteBtn: Button
    lateinit var saveBtn: Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent: TextView
    lateinit var contextEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)

        // View 초기화
        calendarView = root.findViewById(R.id.calendarView)
        diaryTextView = root.findViewById(R.id.diaryTextView)
        saveBtn = root.findViewById(R.id.saveBtn)
        deleteBtn = root.findViewById(R.id.deleteBtn)
        updateBtn = root.findViewById(R.id.updateBtn)
        diaryContent = root.findViewById(R.id.diaryContent)
        contextEditText = root.findViewById(R.id.contextEditText)

        // 캘린더 뷰 설정
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            contextEditText.setText("")
            checkDay(year, month, dayOfMonth, userID)
        }
        // 저장 버튼 설정
        saveBtn.setOnClickListener {
            saveDiary(fname)
            contextEditText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            str = contextEditText.text.toString()
            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE
        }

        updateBtn.setOnClickListener {
            prepareEditDiary()
        }

        deleteBtn.setOnClickListener {
            removeDiary(fname)
            // UI 업데이트 또는 피드백 제공
        }

        return root
    }

    // 날짜 확인 메소드
    private fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        fname = String.format("%s%d-%02d-%02d.txt", userID, cYear, cMonth + 1, cDay)
        try {
            val fileInputStream = requireActivity().openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            val str = String(fileData)

            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE
        } catch (e: FileNotFoundException) {
            diaryContent.text = ""
            diaryContent.visibility = View.INVISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // 일기 제거 메소드
    @SuppressLint("WrongConstant")
    private fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = requireActivity().openFileOutput(readDay, Context.MODE_PRIVATE)
            fileOutputStream.write("".toByteArray()) // 파일 내용을 비움
            fileOutputStream.close()

            diaryContent.text = "" // 텍스트 뷰 내용 비우기
            // UI 업데이트 또는 피드백 제공 (예: 토스트 메시지)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 일기 저장 메소드
    @SuppressLint("WrongConstant")
    private fun saveDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = requireActivity().openFileOutput(readDay, Context.MODE_NO_LOCALIZED_COLLATORS)
            val content = contextEditText.text.toString()
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareEditDiary() {
        contextEditText.setText(diaryContent.text) // 현재 일기 내용을 편집창에 설정
        contextEditText.visibility = View.VISIBLE // 편집창 보이게 설정
        saveBtn.visibility = View.VISIBLE // 저장 버튼 보이게 설정
        updateBtn.visibility = View.INVISIBLE // 수정 버튼 숨기기
        deleteBtn.visibility = View.INVISIBLE // 삭제 버튼 숨기기
        diaryContent.visibility = View.INVISIBLE // 현재 텍스트 뷰 숨기기

        // 저장 버튼에 대한 클릭 리스너 설정
        saveBtn.setOnClickListener {
            editDiary(fname) // 수정된 내용을 저장하는 메소드 호출
        }
    }

    // 일기 수정 메소드
    private fun editDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = requireActivity().openFileOutput(readDay, Context.MODE_PRIVATE)
            val content = contextEditText.text.toString()
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()

            diaryContent.text = content // 텍스트 뷰 업데이트
            diaryContent.visibility = View.VISIBLE
            contextEditText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
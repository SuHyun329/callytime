package com.example.guru_callytime.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guru_callytime.R
import java.util.*

class AlarmFragment : Fragment() {

    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var ampmTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var alarmonoffButton: Button
    private lateinit var alarmsetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alarmViewModel =
            ViewModelProvider(this).get(AlarmViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarm, container, false)



        ampmTextView = root.findViewById<TextView>(R.id.ampmTextView)
        timeTextView = root.findViewById<TextView>(R.id.timeTextView)
        alarmonoffButton = root.findViewById<Button>(R.id.alramonoffButton)
        alarmsetButton = root.findViewById<Button>(R.id.alramsetButton)

        initOnOffButton()
        initChangeAlarmTimeButton()

        val model = fetchDataFromSharedPreferences()

        if (model != null) {
            renderView(model)
        }
        return root
    }

    //alarmdisplaymodel에 있는 데이터 불러와서 업데이트 해서 넣기
    private fun renderView(model: AlarmDisplayModel) {

        ampmTextView.apply {
            text = model.ampmText
        }
        timeTextView.findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }
        alarmonoffButton.findViewById<Button>(R.id.alramonoffButton).apply {
            text = model.onOffText
            tag = model
        }
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel? {
        val sharedPreferences = context?.getSharedPreferences(
            AlarmFragment.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )

        val timeDBValue = sharedPreferences?.getString(AlarmFragment.ALARM_KEY, "09:30") ?: "09:30"
        val onOffDBValue = sharedPreferences?.getBoolean(AlarmFragment.ONOFF_KEY, false)

        val alarmData = timeDBValue.split(":")
        val alarmModel =
            onOffDBValue?.let { AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), it) }


        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) // 있으면 가져오고 없으면 안만든다. (null)

        if ((pendingIntent == null) and (alarmModel?.onOff == true)) {

            alarmModel?.onOff = false

        } else if ((pendingIntent != null) and (alarmModel?.onOff == null)) {
            // 알람을 취소함
            pendingIntent.cancel()
        }
        return alarmModel
    }

    // 시간 재설정 버튼
    private fun initChangeAlarmTimeButton() {

        alarmsetButton.setOnClickListener {
            //현재시간 가져오기
            val calendar = Calendar.getInstance()

            TimePickerDialog(context, { picker, hour, minute ->
                //시간 데이터 모델에 저장
                val model = saveAlarmModel(hour, minute, false)
                //뷰 업데이트
                renderView(model)
                //기존알람삭제
                cancelAlarm()

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()
        }
    }

    // 알람 켜기 끄기 버튼.
    private fun initOnOffButton() {
        alarmonoffButton.setOnClickListener {
            // 저장한 데이터를 확인한다
            val model =
                it.tag as? AlarmDisplayModel ?: return@setOnClickListener// 형변환 실패하는 경우에는 null
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not()) // on off 스위칭
            renderView(newModel)

            // 온/오프 에 따라 작업을 처리한다
            if (newModel.onOff) {
                // 온 -> 알람을 등록
                val calender = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)
                    // 지나간 시간의 경우 다음날 알람으로 울리도록
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1) // 하루 더하기
                    }
                }

                //알람 매니저 가져오기
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                ) // 있으면 새로 만든거로 업데이트

                alarmManager.setInexactRepeating( // 정시에 반복
                    AlarmManager.RTC_WAKEUP, // RTC_WAKEUP : 실제 시간 기준으로 wakeup , ELAPSED_REALTIME_WAKEUP : 부팅 시간 기준으로 wakeup
                    calender.timeInMillis, // 언제 알람이 발동할지.
                    AlarmManager.INTERVAL_DAY, // 하루에 한번씩.
                    pendingIntent
                )
            } else {
                // 오프 -> 알람을 제거
                cancelAlarm()
            }


        }
    }

    private fun cancelAlarm() {
        // 기존에 있던 알람을 삭제한다.

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) // 있으면 가져오고 없으면 안만든다. (null)

        pendingIntent?.cancel() // 기존 알람 삭제
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        // time 에 대한 db 파일 생성
        val sharedPreferences = context?.getSharedPreferences(
            AlarmFragment.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        // edit 모드로 열어서 작업 (값 저장)
        with(sharedPreferences?.edit()) {
            this?.putString(ALARM_KEY, model.makeDataForDB())
            this?.putBoolean(ONOFF_KEY, model.onOff)
            this?.commit()
        }
        return model
    }

    companion object {

        private const val SHARED_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private val ALARM_REQUEST_CODE = 1000


    }
}
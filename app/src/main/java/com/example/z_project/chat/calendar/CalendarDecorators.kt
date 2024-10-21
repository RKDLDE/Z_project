package com.example.z_project.chat.calendar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.Date
import android.graphics.drawable.BitmapDrawable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.z_project.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.prolificinteractive.materialcalendarview.MaterialCalendarView // 캘린더 뷰 관련
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.graphics.Color // 색상 관련
import android.graphics.ColorFilter
import android.graphics.Paint // 페인팅 관련
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.VectorDrawable
import android.text.style.ImageSpan
import androidx.core.graphics.toColorInt
import com.example.z_project.record.FeedModel


val tagMap: MutableMap<DayViewFacade, CalendarDay> = mutableMapOf()

object CalendarDecorators {
    /**
     * 날짜를 표시하는 데 사용되는 요소를 정의하기 위한 함수
     * @param context 리소스에 액세스하기 위해 사용되는 컨텍스트
     * @return DayViewDecorator 객체
     */
    fun dayDecorator(context: Context): DayViewDecorator {
        return object : DayViewDecorator {
            private val drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector)
            override fun shouldDecorate(day: CalendarDay): Boolean = true
            override fun decorate(view: DayViewFacade) {
                view.setSelectionDrawable(drawable!!)
            }
        }
    }

    /**
     * 현재 날짜를 다른 날짜와 구별하기 위해 스타일이나 색상을 적용하기 위한 함수
     * @param context 리소스에 액세스하기 위해 사용되는 컨텍스트
     * @return DayViewDecorator 객체
     */
    fun todayDecorator(context: Context): DayViewDecorator {
        return object : DayViewDecorator {
            private val backgroundDrawable =
                ContextCompat.getDrawable(context, R.drawable.calendar_circle_today)
            private val today = CalendarDay.today()

            override fun shouldDecorate(day: CalendarDay?): Boolean = day == today

            override fun decorate(view: DayViewFacade?) {
                view?.apply {
                    setBackgroundDrawable(backgroundDrawable!!)
                    addSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                    )
                }
            }
        }
    }

    /**
     * 현재 선택된 날 이외의 다른 달의 날짜의 모양을 변경하기 위한 함수
     * @param context 리소스에 액세스하기 위해 사용되는 컨텍스트
     * @param selectedMonth 현재 선택 된 달
     * @return DayViewDecorator 객체
     */
    fun selectedMonthDecorator(context: Context, selectedMonth: Int): DayViewDecorator {
        return object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean = day.month == selectedMonth
            override fun decorate(view: DayViewFacade) {
                view.addSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            R.color.black //선택된 달의 날짜들을 black으로
                        )
                    )
                )
            }
        }
    }

    fun otherMonthDecorator(context: Context, selectedMonth: Int): DayViewDecorator {
        return object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean = day.month != selectedMonth
            override fun decorate(view: DayViewFacade) {
                view.addSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            R.color.gray // 선택되지 않은 달의 날짜들을 회색으로 변경
                        )
                    )
                )
            }
        }
    }

    fun pastAndTodayDecorator(context: Context): DayViewDecorator {
        val today = CalendarDay.today() // 오늘 날짜

        return object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                // 오늘 날짜와 이전 날짜는 데코레이션 대상
                return day.isBefore(today) || day == today
            }

            override fun decorate(view: DayViewFacade) {
                view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.black))) // 검정색
            }
        }
    }

    fun futureDatesDecorator(context: Context): DayViewDecorator {
        val today = CalendarDay.today() // 오늘 날짜

        return object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                // 현재 날짜 이후인 경우 true를 반환
                return day.isAfter(today)
            }

            override fun decorate(view: DayViewFacade) {
                // 날짜를 회색으로 설정
                view.addSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            R.color.gray // 선택되지 않은 달의 날짜들을 회색으로 변경
                        )
                    )
                )
            }
        }
    }


    /**
     * 일요일을 강조하는 데코레이터를 생성하기 위한 함수
     * @return DayViewDecorator 객체
     */
    fun sundayDecorator(context: Context): DayViewDecorator {
        return object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                val calendar = Calendar.getInstance()
                calendar.set(day.year, day.month - 1, day.day)
                return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
            }

            override fun decorate(view: DayViewFacade) {
                view.addSpan(ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                ))
            }
        }
    }

    /**
     * 토요일을 강조하는 데코레이터를 생성하기 위한 함수
     * @return DayViewDecorator 객체
     */
    fun saturdayDecorator(context: Context): DayViewDecorator {
        return object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                val calendar = Calendar.getInstance()
                calendar.set(day.year, day.month - 1, day.day)
                return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
            }

            override fun decorate(view: DayViewFacade) {
                view.addSpan(ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                ))
            }
        }
    }

    fun koreanMonthTitleFormatter(): TitleFormatter {
        return TitleFormatter { day: CalendarDay ->
            val monthNames = arrayOf(
                "1월", "2월", "3월", "4월", "5월", "6월",
                "7월", "8월", "9월", "10월", "11월", "12월"
            )
            monthNames[day.month - 1]
        }
    }

    /**
     * 이벤트가 있는 날짜를 표시하는 데코레이터를 생성하기 위한 함수
     * @param context 리소스에 액세스하기 위해 사용되는 컨텍스트
     * @param scheduleList 이벤트 날짜를 포함하는 스케줄 목록
     * @return DayViewDecorator 객체
     */
    fun eventDecorator(context: Context, scheduleList: List<ScheduleModel>): DayViewDecorator {
        return object : DayViewDecorator {
            private val eventMap = mutableMapOf<CalendarDay, MutableList<Int>>() // 이벤트 날짜마다 여러 색상을 저장하는 맵
            private var eventColors: IntArray = intArrayOf()
            private val dateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault()) // 날짜 포맷 설정

            init {
                if (scheduleList.isEmpty()) {
                    Log.d("eventDecorator", "scheduleList is empty.")
                } else{
                    // 스케줄 목록에서 이벤트가 있는 날짜를 파싱하여 이벤트 날짜 목록에 추가한다.
                    scheduleList.forEach { schedule ->
                        schedule.startDate?.let { startDate ->
                            try {
                                // 시작 날짜를 파싱
                                val startDateTime = dateFormat.parse(startDate)
                                val endDateTime = schedule.endDate?.let { endDate ->
                                    // 종료 날짜를 파싱 (종료 날짜가 없으면 시작 날짜로 설정)
                                    dateFormat.parse(endDate)
                                } ?: startDateTime

                                if (startDateTime != null && startDateTime <= endDateTime) {
                                    // 날짜 범위를 가져와서 이벤트 날짜 목록에 추가
                                    val datesInRange = getDateRange(startDateTime, endDateTime)
                                    //eventDates.addAll(datesInRange)

                                    datesInRange.forEach { date ->
                                        // 유저의 색상 추가
                                        val userColor = schedule.category.let { category ->
                                            val colorEnum = category.getColorEnum()
                                            val colorResId = colorEnum!!.color ?: R.color.main_gray // null일 경우 기본 색상 설정
                                            ContextCompat.getColor(context, colorResId)
                                        }

                                        // 날짜에 색상을 추가 (여러 색상이 있을 경우 리스트로 저장)
                                        userColor.let { color ->
                                            eventMap.computeIfAbsent(date) { mutableListOf() }.add(color)
                                            Log.d("eventMap", "$date: $color")
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }

            }

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return day != null && eventMap.containsKey(day)
            }

            override fun decorate(view: DayViewFacade?) {
                // 이벤트가 있는 날짜에 점을 추가하여 표시
                //view.addSpan(DotSpan(10F, ContextCompat.getColor(context, R.color.black)))

                // 일정 정보가 총합되어서 찍힘.. - 보류ㅠㅠ
//                eventMap.forEach {(calendarDay, colors) ->
//                    val colorList = mutableListOf<Int>()
//
//                    colors.forEach { colorId ->
//                        val colorValue = ContextCompat.getColor(context, colorId) // 색상 ID에서 색상 값으로 변환
//                        colorList.addAll(listOf(colorValue))
//                    }
//                    eventColors = colorList.toIntArray()
//
//                    if (eventColors.isNotEmpty()) {
//                        Log.d("색상들....", "${colorList}")
//                        view!!.addSpan(CustomMultipleDotSpan(6f, eventColors))
//                    } else {
//                        Log.d("DotSpan", "No colors to draw.")
//                    }
//                }
                val colorList = mutableListOf<Int>()
                colorList.addAll(listOf(
                    ContextCompat.getColor(context, R.color.calendar_color_blue),
                    ContextCompat.getColor(context, R.color.calendar_color_yellow),
                    ContextCompat.getColor(context, R.color.calendar_color_orange),
                ))
                eventColors = colorList.toIntArray()
                view!!.addSpan(CustomMultipleDotSpan(6f, eventColors))

            }

            /**
             * 시작 날짜와 종료 날짜 사이의 모든 날짜를 가져오는 함수
             * @param startDate 시작 날짜
             * @param endDate 종료 날짜
             * @return 날짜 범위 목록
             */
            private fun getDateRange(startDate: Date, endDate: Date): List<CalendarDay> {
                val datesInRange = mutableListOf<CalendarDay>()
                val calendar = Calendar.getInstance()
                calendar.time = startDate

                while (calendar.time.before(endDate)) {
                    datesInRange.add(
                        CalendarDay.from(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                    )
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                datesInRange.add(
                    CalendarDay.from(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                ) // Add the end date itself

                return datesInRange
            }
        }
    }


    // 피드 기록 캘린더에 해당하는 이벤트 데코
//    fun recordEventDecorator(context: Context, feedList: List<FeedModel>): DayViewDecorator {
//        return object : DayViewDecorator {
//            private val eventDates = HashMap<CalendarDay, Int>()
//
//            private var currentDay: CalendarDay? = null // 현재 데코레이션할 날짜를 저장
//
//            init {
//                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
//
//                // 스케줄 목록에서 이벤트가 있는 날짜를 파싱하여 이벤트 날짜 목록에 추가
//                feedList.forEach { feed ->
//                    val uploadDateTime = dateFormat.parse(feed.uploadDate)
//                    if (uploadDateTime != null) {
//                        // Date를 Calendar로 변환
//                        val calendar = Calendar.getInstance().apply {
//                            time = uploadDateTime
//                        }
//
//                        // Calendar에서 연도, 월, 일 추출
//                        val year = calendar.get(Calendar.YEAR)
//                        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1 필요
//                        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//                        // CalendarDay 객체로 변환하여 eventDates에 추가
//                        val calendarDay = CalendarDay.from(year, month, day)
//                        eventDates[calendarDay] = feed.uploadEmoji
//                    }
//                }
//            }
//
//            override fun shouldDecorate(day: CalendarDay?): Boolean {
//                return eventDates.contains(day)
//            }
//
//            override fun decorate(view: DayViewFacade) {
//                    eventDates.forEach { day, emojiResId ->
//                        if (shouldDecorate(day)) { // 날짜가 매칭되는지 확인
//                            Log.d("이모지내용", "${emojiResId} for date ${day}")
//
//
//                            val drawable = ContextCompat.getDrawable(context, R.drawable.logo)
//
//                            //ImageSpan 버전
//                            drawable?.setBounds(0, 0, 100, 100)
//                            val imageSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_BOTTOM)
//                            view.addSpan(imageSpan)
//
//
////                        drawable?.let {
////                            view.addSpan(imageSpan)
////                        }
//                        }
//                    }
//
////                // 이벤트가 있는 날짜에 점을 추가하여 표시
////                eventDates.forEach { (calendarDay, emoji) ->
////                    feedList.forEach{ feed ->
////                        if(feed.id == 2){ // 사용자 본인이라면 (예시: 2)
////                            if (view is DayViewFacade) {
////                                view.apply {
////                                    setBackgroundDrawable(ContextCompat.getDrawable(context, feed.uploadEmoji)!!)
////                                    addSpan(
////                                        ForegroundColorSpan(
////                                            ContextCompat.getColor(context, R.color.white)
////                                        )
////                                    )
////                                }
////                            }
////                        } else{
////                            view.addSpan(ForegroundColorSpan(
////                                ContextCompat.getColor(
////                                    context,
////                                    R.color.red
////                                )
////                            ))
////                        }
////                    }
////                }
//            }
//
//            /**
//             * 시작 날짜와 종료 날짜 사이의 모든 날짜를 가져오는 함수
//             * @param startDate 시작 날짜
//             * @param endDate 종료 날짜
//             * @return 날짜 범위 목록
//             */
//            private fun getDateRange(startDate: Date, endDate: Date): List<CalendarDay> {
//                val datesInRange = mutableListOf<CalendarDay>()
//                val calendar = Calendar.getInstance()
//                calendar.time = startDate
//
//                while (calendar.time.before(endDate)) {
//                    datesInRange.add(
//                        CalendarDay.from(
//                            calendar.get(Calendar.YEAR),
//                            calendar.get(Calendar.MONTH) + 1,
//                            calendar.get(Calendar.DAY_OF_MONTH)
//                        )
//                    )
//                    calendar.add(Calendar.DAY_OF_MONTH, 1)
//                }
//                datesInRange.add(
//                    CalendarDay.from(
//                        calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH) + 1,
//                        calendar.get(Calendar.DAY_OF_MONTH)
//                    )
//                ) // Add the end date itself
//
//                return datesInRange
//            }
//        }
    //}
}

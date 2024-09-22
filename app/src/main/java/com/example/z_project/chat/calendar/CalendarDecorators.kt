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
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    fun PaddingDayViewDecorator(context: Context, calendarDay: CalendarDay): DayViewDecorator{
        return object : DayViewDecorator{
            private val inflater: LayoutInflater = LayoutInflater.from(context)

            override fun decorate(view: DayViewFacade) {
                // 날짜 커스텀 레이아웃으로 설정
                val dayView = inflater.inflate(R.layout.custom_day_view, null)
                val dayText: TextView = dayView.findViewById(R.id.dayText)

                dayText.text = calendarDay.day.toString()

                // 뷰의 사이즈 측정
                dayView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                dayView.layout(0, 0, dayView.measuredWidth, dayView.measuredHeight)

                // 뷰를 비트맵으로 변환
                val bitmap = viewToBitmap(dayView)
                val drawable = bitmapToDrawable(context, bitmap)

                // 날짜 뷰 설정
                view.apply {
                    // 커스텀 스타일 적용
                    setBackgroundDrawable(drawable)
                }
            }

            override fun shouldDecorate(day: CalendarDay): Boolean = true
        }
    }
    fun viewToBitmap(view: View): Bitmap {
        val width = view.width
        val height = view.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    fun bitmapToDrawable(context: Context, bitmap: Bitmap): BitmapDrawable {
        return BitmapDrawable(context.resources, bitmap)
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
            private lateinit var colors: IntArray
            private val eventDates = HashSet<CalendarDay>()

            init {
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

                // 스케줄 목록에서 이벤트가 있는 날짜를 파싱하여 이벤트 날짜 목록에 추가한다.
                scheduleList.forEach { schedule ->
                    schedule.startDate?.let { startDate ->
//                        try {
//                            // 시작 날짜를 파싱
//                            val startDateTime = dateFormat.parse(startDate)
//                            val endDateTime = schedule.endDate?.let { endDate ->
//                                // 종료 날짜를 파싱 (종료 날짜가 없으면 시작 날짜로 설정)
//                                dateFormat.parse(endDate)
//                            } ?: startDateTime
//
//                            // 날짜 범위를 가져와서 이벤트 날짜 목록에 추가
//                            val datesInRange = getDateRange(startDateTime!!, endDateTime)
//                            eventDates.addAll(datesInRange)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }

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
                                    val userColor = schedule.userColor?.color ?: ContextCompat.getColor(context, R.color.select_blue)

                                    // 날짜에 색상을 추가 (여러 색상이 있을 경우 리스트로 저장)
                                    eventMap.computeIfAbsent(date) { mutableListOf() }.add(userColor)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun shouldDecorate(day: CalendarDay?): Boolean {
//                return eventDates.contains(day)
                return day != null && eventMap.containsKey(day)
                //return eventMap.containsKey(day)
            }

            override fun decorate(view: DayViewFacade) {
                // 이벤트가 있는 날짜에 점을 추가하여 표시
                //view.addSpan(DotSpan(10F, ContextCompat.getColor(context, R.color.black)))

                Log.d("이벤트내역", "${eventMap}")

                val dotList = mutableListOf<Int>()
                // eventMap을 순회하며 색상을 추가
                eventMap.forEach { (_, colorList) ->
                    dotList.addAll(colorList) // 각 날짜의 색상을 colorsList에 추가
                }
                colors = dotList.toIntArray()
                Log.d("색깔리스트", "${colors.contentToString()}")
                view.addSpan(CustomMultipleDotSpan(6f, colors))

//                // 해당 날짜에 여러 색상의 점을 표시
//                eventMap.forEach { (calendarDay, colors) ->
//                    eventMap.keys.forEach { day ->
//                        if (view.toString() == calendarDay.toString()) {
//                            val colors = eventMap[day] // 현재 날짜에 대한 색상 목록을 가져옴
//                            colors?.forEach { color ->
//                                Log.d("dot 색상", "$color")
//                                view.addSpan(DotSpan(6F, ContextCompat.getColor(context, color))) // 각 색상에 대해 점 추가
//                            }
//                        }
//                    }
////                    colors.forEach { color ->
////                        Log.d("dot 색상", "${color}")
////                        view.addSpan(DotSpan(6F, ContextCompat.getColor(context, color)))
////                    }
//                }
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

//                while (calendar.time.before(endDate) || calendar.time == endDate) {
//                    datesInRange.add(
//                        CalendarDay.from(
//                            calendar.get(Calendar.YEAR),
//                            calendar.get(Calendar.MONTH) + 1,
//                            calendar.get(Calendar.DAY_OF_MONTH)
//                        )
//                    )
//                    calendar.add(Calendar.DAY_OF_MONTH, 1)
//                }
                return datesInRange
            }
        }
    }
}

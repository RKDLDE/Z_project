package com.example.z_project.chat.calendar

data class CalendarModel(
    val groupId: Int?,
    val events: List<ScheduleModel>,
    val eventCategorys: List<Categorys>
)

data class ScheduleModel(
    //val key: String?,
    val authId: Int?, //개인 user Id
    val groupId: Int?, //그룹방 Id
    var title: String?, //일정 내용
    val startDate: String?,
    val endDate: String?,
    val startTime: String,
    val endTime: String,
    val category: Categorys
    //val description: String?,
    //val buttonUiState: CalendarButtonUiState?
)

data class Categorys(
    val name: String, // 카테고리 이름
    val color: ColorEnum? // 카테고리 색상 (예: Color.RED)
)
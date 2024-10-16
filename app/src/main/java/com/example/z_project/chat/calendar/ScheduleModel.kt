package com.example.z_project.chat.calendar

data class CalendarModel(
    val groupId: Int?,
    val events: List<ScheduleModel>,
    val eventCategorys: List<Categories>
)

data class ScheduleModel(
    //val key: String?,
    val authId: String?, //개인 user Id
    val groupId: String?, //그룹방 Id
    var title: String?, //일정 내용
    val startDate: String?,
    val endDate: String?,
    val startTime: String,
    val endTime: String,
    val category: Categories
    //val description: String?,
    //val buttonUiState: CalendarButtonUiState?
)

data class Categories(
    val authId: String?,
    val groupId: String?,
    val name: String?, // 카테고리 이름
    val color: String? // 카테고리 색상 (예: Color.RED)
)
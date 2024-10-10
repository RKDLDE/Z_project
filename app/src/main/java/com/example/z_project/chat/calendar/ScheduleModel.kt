package com.example.z_project.chat.calendar

data class ScheduleModel(
    //val key: String?,
    val authId: Int?, //개인 user Id
    val groupId: Int?, //그룹방 Id
    var title: String?, //일정 내용
    val startDate: String?,
    val endDate: String?,
    val startTime: String,
    val endTime: String,
    val userColor: ColorEnum?,
    //val description: String?,
    //val buttonUiState: CalendarButtonUiState?
)

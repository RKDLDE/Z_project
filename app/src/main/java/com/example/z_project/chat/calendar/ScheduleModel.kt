package com.example.z_project.chat.calendar

data class ScheduleModel(
    //val key: String?,
    val authId: Int?, //개인 user Id
    val groupId: Int?, //그룹방 Id
    val startDate: String?,
    val endDate: String?,
    val userColor: ColorEnum?,
    val title: String?, //일정 내용
    //val description: String?,
    //val buttonUiState: CalendarButtonUiState?
)

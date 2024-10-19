package com.example.z_project.chat.calendar

data class CalendarModel(
    val groupId: Int? = null,
    val events: List<ScheduleModel>? = null,
    val eventCategorys: List<Categories>? = null
)

data class ScheduleModel(
    //val key: String?,
    val authId: String? = null, //개인 user Id
    val groupId: String? = null, //그룹방 Id
    var documentId: String? = null, //일정 문서 Id
    var title: String? = null, //일정 내용
    val startDate: String? = null,
    val endDate: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val category: Categories = Categories(),
    //val description: String?,
    //val buttonUiState: CalendarButtonUiState?
)

data class Categories(
    val authId: String? = null,
    val groupId: String? = null,
    var categoryId: String? = null,
    val name: String? = null, // 카테고리 이름
    val color: String? = null, // 카테고리 색상 (예: Color.RED)
){
    // 색상 문자열을 ColorEnum으로 변환하는 함수
    fun getColorEnum(): ColorEnum {
        return when (color) {
            "RED" -> ColorEnum.RED
            "ORANGE" -> ColorEnum.ORANGE
            "YELLOW" -> ColorEnum.YELLOW
            "GREEN" -> ColorEnum.GREEN
            "BLUE" -> ColorEnum.BLUE
            "PINK" -> ColorEnum.PINK
            "PURPLE" -> ColorEnum.PURPLE
            "GRAY" -> ColorEnum.GRAY
            else -> ColorEnum.UNKNOWN
        }
    }
}
package com.example.z_project.calendar

import androidx.annotation.StringRes
import com.example.z_project.R

enum class ColorEnum (
    //@StringRes val title: Int?,
    @StringRes val color: Int?,
) {
    RED(
        //title = R.string.util_dialog_color_red,
        color = R.color.calendar_color_red,
    ),
    ORANGE(
        //title = R.string.util_dialog_color_orange,
        color = R.color.calendar_color_orange,
    ),
    YELLOW(
        //title = R.string.util_dialog_color_orange,
        color = R.color.calendar_color_yellow,
    ),
    GREEN(
        //title = R.string.util_dialog_color_green,
        color = R.color.calendar_color_green,
    ),
    BLUE(
        //title = R.string.util_dialog_color_blue,
        color = R.color.calendar_color_blue,
    ),
    PINK(
        //title = R.string.util_dialog_color_orange,
        color = R.color.calendar_color_pink,
    ),
    PURPLE(
        //title = R.string.util_dialog_color_purple,
        color = R.color.calendar_color_purple,
    ),
    GRAY(
        //title = R.string.util_dialog_color_gray,
        color = R.color.calendar_color_gray,
    ),
    UNKNOWN(
        //title = R.string.util_dialog_color_unknown,
        color = R.color.main_gray,
    );

    companion object {
        fun getByColor(color: Int): ColorEnum? {
            return values().find { it.color == color }
        }
    }
}
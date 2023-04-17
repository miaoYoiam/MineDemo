package com.example.minedemo.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.minedemo.R
import kotlinx.android.synthetic.main.item_widget_lucky_draw_child.view.*

/**
 * Day：4/26/21 3:08 PM
 * @author zhanglei
 *
 *  @see R.layout.item_widget_lucky_draw_child
 */
class LuckyDrawChildWidget : ConstraintLayout {

    private var defaultBgColor: Int = R.color.bg_D7EEFF
    private var selectedBgColor = defaultBgColor
    private var defaultText: String = "x10"

    constructor(context: Context) : this(context, null)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, 0)

    fun setConfig(icon: Int, text: String, defaultBgColor: Int, selectedBgColor: Int) {
        this.defaultText = text
        this.defaultBgColor = defaultBgColor
        this.selectedBgColor = selectedBgColor

        child_icon.setImageResource(icon)
        child_text.text = defaultText
        lucky_child_root.setBackgroundColor(defaultBgColor)
    }


    fun refresh(isSelect: Boolean) {
        if (isSelect) {
            lucky_child_root.setBackgroundColor(selectedBgColor)
        } else {
            lucky_child_root.setBackgroundColor(defaultBgColor)
        }
    }
}
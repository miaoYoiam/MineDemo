package com.example.minedemo.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.example.lib_annotation.PagePath
import com.example.lib_annotation.PageType
import com.example.minedemo.R
import com.example.minedemo.data.Text
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

@PagePath(id = "main2", type = PageType.IActivity, allowMultiMode = false)
class MainActivity2 : AppCompatActivity() {
    private var mProgress = 0f

    private val aVocal = mutableListOf<Int>()
    private val aVocalRange = mutableListOf<IntRange>()
    private var text: Text? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//        handler.sendEmptyMessage(0)
        initIntent(savedInstanceState)

        initSpan()
        val tempList = LinkedList<Int>()
        tempList.add(0)
        tempList.add(1)
        tempList.add(2)
        tempList.add(3)
        tempList.add(4)

        val sub = tempList.subList(0, 3)
        println("sub====>${sub}")

        aVocal.add(0)
        aVocal.add(1)

        aVocal.add(3)
        aVocal.add(4)

        aVocal.add(7)

        aVocal.add(9)

        aVocal.add(15)

        aVocal.add(21)
        aVocal.add(22)
        aVocal.add(23)
        aVocal.add(24)

        aVocal.add(27)
        aVocal.add(28)
        aVocal.add(29)
        aVocal.add(30)
        aVocal.add(31)
        aVocal.add(32)

        aVocal.add(41)

        aVocal.add(46)
        aVocal.add(47)
        aVocal.add(48)
        aVocal.add(49)
        aVocal.add(50)


        println("----->${aVocalRange.toString()}")

    }

    private fun initSpan() {
        text_span.text=processInviteDes("小丸子","邀請你加入","米津玄師的家族")
    }

    private fun processInviteDes(user: String?, actionText: String?, family: String?): SpannableString {
        val builder = SpannableStringBuilder()
        val inviterName = user
        val inviterNameLength = inviterName?.length ?: 0
        val inviterNameColor = ContextCompat.getColor(this, R.color.white)
        if (inviterNameLength > 0) {
            val start = builder.length
            builder.append("$inviterName  ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.setSpan(ForegroundColorSpan(inviterNameColor), start, start + inviterNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val actionTextLength = actionText?.length ?: 0
        val actionTextColor = ContextCompat.getColor(this, R.color.white_alpha_50)
        if (inviterNameLength > 0) {
            val start = builder.length
            builder.append("$actionText  ")
            builder.setSpan(ForegroundColorSpan(actionTextColor), start, start + actionTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val familyName = family
        val familyNameLength = familyName?.length ?: 0
        val familyNameColor = ContextCompat.getColor(this, R.color.blue_6)
        if (inviterNameLength > 0) {
            val start = builder.length
            builder.append(familyName)
            builder.setSpan(ForegroundColorSpan(familyNameColor), start, start + familyNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }


        return SpannableString(builder)
    }

    private fun initIntent(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            if (intent != null) {
                text = intent.getParcelableExtra<Text>("Test")
            }
        } else {
            System.out.println("-=-------->")
            savedInstanceState.classLoader = classLoader.parent
            text = savedInstanceState.getParcelable("Test") as? Text
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.putParcelable("Test", text)
        super.onSaveInstanceState(outState)
    }

    fun testRetainAll(view: View) {
        val list1 = mutableListOf<String>(
            "nll",
            "wwode 我的",
            "111",
            "222",
            "333",
            "444",
            "555",
            "666",
            "777",
            "888",
            "999"
        )
        val list2 = mutableListOf<String>(
            "111",
            "222",
            "3333",
            "4444",
            "5555",
            "6666",
            "7777",
            "8888",
            "9999"
        )
        list1.removeAll(list2)
        list1.addAll(list2)
        println("======> ${list1}")
    }

    private fun checkUidsEquals(curIds: List<String>?, otherIds: List<String>?): Boolean {
        if (curIds?.size ?: 0 == otherIds?.size ?: 0) {
            val curId0 = curIds?.getOrNull(0)
            val curId1 = curIds?.getOrNull(1)

            val otherId0 = otherIds?.getOrNull(0)
            val otherId1 = otherIds?.getOrNull(1)

            var isEqual = true

            if (curId0 != otherId0 && curId0 != otherId1) {
                isEqual = false
            }

            if (curId1 != otherId0 && curId1 != otherId1) {
                isEqual = false
            }
            return isEqual

        } else {
            return false
        }
    }
}
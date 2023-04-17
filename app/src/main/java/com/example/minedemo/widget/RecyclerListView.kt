package com.example.minedemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

class RecyclerListView : RecyclerView {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        0
    ) {
        addOnItemTouchListener(RecyclerListViewItemClickListener(context))
    }

    private var onItemTouchMoveListener: OnItemTouchMoveListener? = null

    private var startEdge: Int = 0
    private var endEdge: Int = 200

    private var currentChildView: View? = null
    private var currentChildPosition = -1
    private var interceptedByChild = false
    private var interceptedScroll = false

    private var lastTouchPosition = currentChildPosition

    inner class RecyclerListViewItemClickListener(context: Context?) : OnItemTouchListener {
        override fun onInterceptTouchEvent(
            view: RecyclerView,
            event: MotionEvent
        ): Boolean {
            val action = event.actionMasked
            val isScrollIdle = this@RecyclerListView.scrollState == SCROLL_STATE_IDLE
            if ((action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_MOVE) && currentChildView == null && isScrollIdle) {
                val ex = event.x
                val ey = event.y
                interceptedScroll = ex > startEdge && ex < endEdge

                val animator = itemAnimator
                if (animator == null || !animator.isRunning) {
                    currentChildView = findChildViewUnder(ex, ey)
                }
                currentChildPosition = -1
                currentChildView?.let {
                    currentChildPosition = view.getChildPosition(it)
                    lastTouchPosition = currentChildPosition
                    onItemTouchMoveListener?.onMove(it, currentChildPosition, ex, ey)

//                    val childEvent = MotionEvent.obtain(
//                        0,
//                        0,
//                        event.actionMasked,
//                        event.x - it.left,
//                        event.y - it.top,
//                        0
//                    )
//                    if (it.onTouchEvent(childEvent)) {
//                        interceptedByChild = true
//                    }
//                    childEvent.recycle()
                }

            }

            if (action == MotionEvent.ACTION_MOVE && !interceptedByChild && interceptedScroll) {
                val ex = event.x
                val ey = event.y
                currentChildView = findChildViewUnder(ex, ey)
                currentChildView?.let {
                    currentChildPosition = view.getChildPosition(it)
                    if (currentChildPosition != lastTouchPosition) {
                        lastTouchPosition = currentChildPosition
                        onItemTouchMoveListener?.onMove(it, currentChildPosition, ex, ey)
                    }
                }
            }

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_CANCEL) {
                if (currentChildView != null) {
                    currentChildView = null
                    interceptedByChild = false
                    lastTouchPosition = -1
                    currentChildPosition = -1
                    if (interceptedScroll) {
                        interceptedScroll = false
                        return true
                    }
                }
            }
            return false
        }

        override fun onTouchEvent(view: RecyclerView, event: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            cancelTouchRequest()
        }
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        if (interceptedScroll) {
            consumed!![0] = dx
            consumed[1] = dy
            return true
        }
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }


    fun cancelTouchRequest() {
        if (currentChildView != null) {
            currentChildView = null
        }
        interceptedByChild = false
        interceptedScroll = false
    }

    fun setTouchEdge(start: Int, end: Int) {
        this.startEdge = start
        this.endEdge = end
    }

    interface OnItemTouchMoveListener {
        fun onMove(view: View?, position: Int, dx: Float, dy: Float)
    }


    fun setOnItemLongClickListener(listener: OnItemTouchMoveListener) {
        onItemTouchMoveListener = listener
    }
}
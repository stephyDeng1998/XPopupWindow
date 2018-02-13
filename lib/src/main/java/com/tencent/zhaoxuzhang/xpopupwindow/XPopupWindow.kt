package com.tencent.zhaoxuzhang.demo

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import com.tencent.zhaoxuzhang.xpopupwindow.util.MeasureUtil
import com.tencent.zhaoxuzhang.xpopupwindow.util.ScreenUtil

/**
 * Created by zhaoxuzhang on 2018/1/26.
 */

abstract class XPopupWindow : PopupWindow {

    private val TAG = "XPopupWindow"

    private lateinit var mCtx: Context

    private lateinit var mPopupView: View

    private lateinit var mInflater: LayoutInflater


    constructor(ctx: Context) {
        init(ctx, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    constructor(ctx: Context, w: Int, h: Int) : this(ctx) {
        init(ctx, w, h)
    }

    private fun init(ctx: Context, w: Int, h: Int) {
        mCtx = ctx
        mInflater = LayoutInflater.from(ctx)

        mPopupView = mInflater.inflate(getLayoutId(), null)
        mPopupView.isFocusableInTouchMode = true

        contentView = mPopupView

        width = w
        height = h

        // 测量宽高

        mPopupView.measure(MeasureUtil.makeMeasureSpec(width), MeasureUtil.makeMeasureSpec(height))

        contentView.measure(MeasureUtil.makeMeasureSpec(width), MeasureUtil.makeMeasureSpec(height))

        initViews(mPopupView)

        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())

        if (startAnim() != 0) {
            animationStyle = startAnim()
        }

        mPopupView.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            val height = mPopupView.findViewById<View>(getLayoutParentNodeId()).top
            val y = motionEvent.y
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            true
        })

    }

    override fun dismiss() {
        super.dismiss()
        exitAnim()
    }

    fun showPopupFromScreenBottom(layoutId: Int) {
        showAtLocation(mInflater.inflate(layoutId, null), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
    }

    fun showPopupFromScreenTop(layoutId: Int) {
        showAtLocation(mInflater.inflate(layoutId, null), Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
    }

    fun showPopupFromScreenLeft(layoutId: Int) {
        showAtLocation(mInflater.inflate(layoutId, null), Gravity.LEFT or Gravity.CENTER_VERTICAL, 0, 0)
    }

    fun showPopupFromScreenRight(layoutId: Int) {
        showAtLocation(mInflater.inflate(layoutId, null), Gravity.RIGHT or Gravity.CENTER_VERTICAL, 0, 0)
    }

    fun showPopupAtViewBottom(view: View, isShowFully: Boolean = false) {
        var offsetX = (view.width - contentView.measuredWidth) / 2
        var offsetY = 0
        if (isShowFully) {
            var viewLoc = intArrayOf(0, 0)
            view.getLocationOnScreen(viewLoc)
            if (ScreenUtil.getScreenHeight(mCtx) - viewLoc[1] - view.height > contentView.measuredHeight) {
                offsetY = 0
            } else {
                this.showPopupAtViewTop(view)
            }
        } else {
            offsetY = 0
        }
        showAsDropDown(view, offsetX, offsetY, Gravity.START)
    }

    fun showPopupAtViewTop(view: View, isShowFully: Boolean = false) {
        var offsetX = (view.width - contentView.measuredWidth) / 2
        var offsetY = 0
        if (isShowFully) {
            var viewLoc = intArrayOf(0, 0)
            view.getLocationOnScreen(viewLoc)
            if (viewLoc[1] > contentView.measuredHeight) {
                offsetY = -(contentView.measuredHeight + view.height)
            } else {
                this.showPopupAtViewBottom(view)
            }
        } else {
            offsetY = -(contentView.measuredHeight + view.height)
        }
        showAsDropDown(view, offsetX, offsetY, Gravity.START)
    }

    fun showPopupAtViewLeft(view: View) {
        var offsetX = -contentView.measuredWidth
        var offsetY = -(view.height + contentView.measuredHeight) / 2
        showAsDropDown(view, offsetX, offsetY, Gravity.START)
    }

    fun showPopupAtViewRight(view: View) {
        var offsetX = view.measuredWidth
        var offsetY = -(view.height + contentView.measuredHeight) / 2
        showAsDropDown(view, offsetX, offsetY, Gravity.START)
    }

    fun getMeasuredWidth(): Int {
        return contentView.measuredWidth
    }

    fun getMeasuredHeight(): Int {
        return contentView.measuredHeight
    }

    abstract fun getLayoutId(): Int

    abstract fun getLayoutParentNodeId(): Int // ??

    abstract fun initViews(view: View)

    abstract fun startAnim(): Int

    abstract fun exitAnim()

}
package com.example.androidpracticumcustomview.ui.theme

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxWidth = 0
        var totalHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            maxWidth = maxOf(maxWidth, child.measuredWidth)
            totalHeight += child.measuredHeight
        }

        val width = resolveSize(maxWidth, widthMeasureSpec)
        val height = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        if (childCount > 2) throw IllegalStateException("CustomContainer can only have 2 children.")

        val parentWidth = measuredWidth
        val parentHeight = measuredHeight

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val localLeft = (parentWidth - childWidth) / 2
            val localTop = if (i == 0) 0 else parentHeight - childHeight

            child.layout(localLeft, localTop, localLeft + childWidth, localTop + childHeight)
        }
    }

    private fun startAppearanceAnimation(view: View, isFirst: Boolean) {
        view.alpha = 0f
        val targetAlpha = 1f
        val initialY = measuredHeight / 2f
        val targetY = if (isFirst) 0f else (measuredHeight - view.measuredHeight).toFloat()

        val translationAnimator = ObjectAnimator.ofFloat(view, "y", initialY, targetY).apply {
            duration = 5000
        }
        val alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, targetAlpha).apply {
            duration = 3000
        }
        AnimatorSet().apply {
            playTogether(alphaAnimator, translationAnimator)
            start()
        }
    }

    override fun addView(child: View?) {
        if (childCount >= 2) throw IllegalStateException("CustomContainer can only have 2 children.")
        super.addView(child)
        if (child != null) {
            post {
                startAppearanceAnimation(child, childCount == 1)
            }
        }
    }
}
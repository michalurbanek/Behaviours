package com.legalzoom.kollaborate.base.widget

import android.content.Context
import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.WindowInsetsCompat
import android.util.AttributeSet
import android.view.View


abstract class VerticalScrollBehavior<V : View> : CoordinatorLayout.Behavior<V> {
	private var totalDyUnconsumed = 0
	private var totalDy = 0
	private var overScrollDirection = ScrollDirection.SCROLL_NONE
	private var scrollDirection = ScrollDirection.SCROLL_NONE

	enum class ScrollDirection {
		SCROLL_DIRECTION_UP,
		SCROLL_DIRECTION_DOWN,
		SCROLL_NONE
	}


	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


	constructor() : super()


	/**
	 * @param coordinatorLayout
	 * @param child
	 * @param direction         Direction of the overscroll: SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN
	 * @param currentOverScroll Unconsumed value, negative or positive based on the direction;
	 * @param totalOverScroll   Cumulative value for current direction
	 */
	internal abstract fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: V, direction: ScrollDirection, currentOverScroll: Int, totalOverScroll: Int)


	/**
	 * @param scrollDirection Direction of the overscroll: SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN
	 */
	internal abstract fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, scrollDirection: ScrollDirection)


	internal abstract fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float, scrollDirection: ScrollDirection): Boolean


	override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) nestedScrollAxes and View.SCROLL_AXIS_VERTICAL != 0 else nestedScrollAxes != 0

	}


	override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, nestedScrollAxes: Int) {
		super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
	}


	override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View) {
		super.onStopNestedScroll(coordinatorLayout, child, target)
	}


	override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
		if (dyUnconsumed > 0 && totalDyUnconsumed < 0) {
			totalDyUnconsumed = 0
			overScrollDirection = ScrollDirection.SCROLL_DIRECTION_UP
		} else if (dyUnconsumed < 0 && totalDyUnconsumed > 0) {
			totalDyUnconsumed = 0
			overScrollDirection = ScrollDirection.SCROLL_DIRECTION_DOWN
		}
		totalDyUnconsumed += dyUnconsumed
		onNestedVerticalOverScroll(coordinatorLayout, child, overScrollDirection, dyConsumed, totalDyUnconsumed)
	}


	override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray) {
		super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
		if (dy > 0 && totalDy < 0) {
			totalDy = 0
			scrollDirection = ScrollDirection.SCROLL_DIRECTION_UP
		} else if (dy < 0 && totalDy > 0) {
			totalDy = 0
			scrollDirection = ScrollDirection.SCROLL_DIRECTION_DOWN
		}
		totalDy += dy
		onDirectionNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, scrollDirection)
	}


	override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
		super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
		scrollDirection = if (velocityY > 0) ScrollDirection.SCROLL_DIRECTION_UP else ScrollDirection.SCROLL_DIRECTION_DOWN
		return onNestedDirectionFling(coordinatorLayout, child, target, velocityX, velocityY, scrollDirection)
	}


	override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float): Boolean {
		return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
	}


	override fun onApplyWindowInsets(coordinatorLayout: CoordinatorLayout?, child: V?, insets: WindowInsetsCompat): WindowInsetsCompat {
		return super.onApplyWindowInsets(coordinatorLayout, child, insets)
	}
}

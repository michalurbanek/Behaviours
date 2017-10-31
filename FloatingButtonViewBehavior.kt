package com.legalzoom.kollaborate.base.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View


class FloatingButtonViewBehavior : FloatingActionButton.Behavior {

	companion object {
		fun <V : View> from(view: V): FloatingButtonViewBehavior {
			val params = view.layoutParams as? CoordinatorLayout.LayoutParams ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
			return params.behavior as? FloatingButtonViewBehavior ?: throw IllegalArgumentException(
					"The view is not associated with BottomNavigationBehavior")
		}
	}


	constructor() : super()


	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


	override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
	}


	override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes, type)
	}


	override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
		handleVisibility(dyConsumed, child)
	}


	override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
		handleVisibility(dyConsumed, child)
	}

	private fun handleVisibility(dyConsumed: Int, child: FloatingActionButton) {
		if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
			child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
				override fun onHidden(fab: FloatingActionButton?) {
					super.onHidden(fab)
					child.visibility = View.INVISIBLE
				}
			})
		} else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
			child.show()
		}
	}
}

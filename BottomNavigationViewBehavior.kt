package com.legalzoom.kollaborate.base.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.legalzoom.kollaborate.base.LegalZoomBase
import com.legalzoom.kollaborate.base.R


class BottomNavigationViewBehavior : VerticalScrollBehavior<View> {
	companion object {
		private val INTERPOLATOR = LinearOutSlowInInterpolator()


		fun <V : View> from(view: V): BottomNavigationViewBehavior {
			val params = view.layoutParams as? CoordinatorLayout.LayoutParams ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
			return params.behavior as? BottomNavigationViewBehavior ?: throw IllegalArgumentException(
					"The view is not associated with BottomNavigationBehavior")
		}
	}

	private val defaultOffset: Int
	private val bottomNavHeight: Int
	private var hidden = false
	private var mTranslationAnimator: ViewPropertyAnimatorCompat? = null


	constructor() : super() {
		this.bottomNavHeight = LegalZoomBase.getContext().resources.getDimensionPixelSize(R.dimen.global_bottom_bar_height)
		this.defaultOffset = 0
	}


	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		this.bottomNavHeight = LegalZoomBase.getContext().resources.getDimensionPixelSize(R.dimen.global_bottom_bar_height)
		this.defaultOffset = 0
	}


	override fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: View, direction: ScrollDirection, currentOverScroll: Int, totalOverScroll: Int) {}


	override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, scrollDirection: ScrollDirection) {
		if (target is RecyclerView && isRecyclerVerticallyScrollable(target))
			handleDirection(child, scrollDirection)
		else if (target !is RecyclerView)
			handleDirection(child, scrollDirection)
	}


	override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, scrollDirection: ScrollDirection): Boolean {
		if (target is RecyclerView && isRecyclerVerticallyScrollable(target))
			handleDirection(child, scrollDirection)
		else if (target !is RecyclerView)
			handleDirection(child, scrollDirection)
		return true
	}


	fun setExpanded(child: View, expanded: Boolean?) {
		if (expanded!!) {
			hidden = false
			animateOffset(child, defaultOffset)
		} else {
			hidden = true
			animateOffset(child, bottomNavHeight + defaultOffset)
		}
	}


	fun isRecyclerVerticallyScrollable(recyclerView: RecyclerView): Boolean {
		return recyclerView.computeVerticalScrollRange() > recyclerView.height
	}


	private fun handleDirection(child: View, scrollDirection: ScrollDirection) {
		if (scrollDirection == VerticalScrollBehavior.ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
			hidden = false
			animateOffset(child, defaultOffset)
		} else if (scrollDirection == VerticalScrollBehavior.ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
			hidden = true
			animateOffset(child, bottomNavHeight + defaultOffset)
		}
	}


	private fun animateOffset(child: View, offset: Int) {
		ensureOrCancelAnimator(child)
		mTranslationAnimator!!.translationY(offset.toFloat()).start()
	}


	private fun ensureOrCancelAnimator(child: View) {
		if (mTranslationAnimator == null) {
			mTranslationAnimator = ViewCompat.animate(child)
			mTranslationAnimator!!.duration = 300
			mTranslationAnimator!!.interpolator = INTERPOLATOR
		} else {
			mTranslationAnimator!!.cancel()
		}
	}
}

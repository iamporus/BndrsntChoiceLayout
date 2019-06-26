package com.prush.bndrsntchoicelayout

import android.animation.ObjectAnimator
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.prush.bndrsntchtimer.BndrsntchTimer

class BndrsntChoiceLayout : RelativeLayout {

    private val choiceLayout: LinearLayout
    private val choiceOneTextView: TextView
    private val choiceTwoTextView: TextView
    private val singleChoiceTextView: TextView
    private val bndrsntchTimer: BndrsntchTimer

    private var numberOfChoices: Int = 0
    private var choiceOneText: String? = null
    private var choiceTwoText: String? = null
    private var choiceThreeText: String? = null
    private var bRevealMode: Boolean = false

    /**
     * Callback to be invoked when Timer is elaspsed.
     */
    interface OnTimerElapsedListener {
        /**
         * Notifies the implementer when timer has elapsed.
         *
         */
        fun onTimerElapsed()
    }

    constructor(context: Context) :
            this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) :
            this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        LayoutInflater.from(context).inflate(R.layout.choice_layout, this, true)

        singleChoiceTextView = findViewById(R.id.choice_single_textview)
        choiceLayout = findViewById(R.id.linear_layout)
        choiceOneTextView = findViewById(R.id.choice_one_textview)
        choiceTwoTextView = findViewById(R.id.choice_two_textview)
        bndrsntchTimer = findViewById(R.id.timer)

        if (attributeSet != null) {

            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BndrsntChoiceLayout, 0, defStyle)

            numberOfChoices = typedArray.getInt(R.styleable.BndrsntChoiceLayout_no_of_choices, 0)

            choiceOneText = typedArray.getString(R.styleable.BndrsntChoiceLayout_choice_one_text)
            choiceTwoText = typedArray.getString(R.styleable.BndrsntChoiceLayout_choice_two_text)
            choiceThreeText = typedArray.getString(R.styleable.BndrsntChoiceLayout_choice_three_text)

            bRevealMode = typedArray.getBoolean(R.styleable.BndrsntChoiceLayout_reveal_mode, false)

            typedArray.recycle()

            when (numberOfChoices) {
                1 -> {
                    choiceLayout.visibility = View.GONE
                    singleChoiceTextView.text = choiceOneText
                }
                2 -> {
                    choiceLayout.visibility = View.VISIBLE
                    singleChoiceTextView.visibility = View.GONE

                    choiceOneTextView.text = choiceOneText
                    choiceTwoTextView.text = choiceTwoText
                }
                3 -> {
                    choiceLayout.visibility = View.VISIBLE
                    singleChoiceTextView.visibility = View.VISIBLE

                    choiceOneTextView.text = choiceOneText
                    choiceTwoTextView.text = choiceTwoText
                    singleChoiceTextView.text = choiceThreeText
                }
            }

            // keep the view hidden in the beginning
            alpha = if (bRevealMode) 0f else 1f
        }
    }

    public fun startTimer(duration: Long, onTimerElapsedListener: OnTimerElapsedListener) {

        if (bRevealMode) {
            val objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
            objectAnimator.duration = 2000

            objectAnimator.addUpdateListener {
                val value = it.animatedValue as Float
                if (value >= 1) {

                    startBndrsntchTimer(duration, onTimerElapsedListener)
                }

            }
            objectAnimator.start()

        } else {
            startBndrsntchTimer(duration, onTimerElapsedListener)
        }
    }

    private fun startBndrsntchTimer(
        duration: Long,
        onTimerElapsedListener: OnTimerElapsedListener
    ) {
        bndrsntchTimer.start(duration) { elapsedDuration, totalDuration ->

            if (elapsedDuration >= totalDuration) {
                onTimerElapsedListener.onTimerElapsed()
            }
        }
    }


    fun getLifeCycleObserver(): LifecycleObserver {
        return bndrsntchTimer.lifecycleObserver
    }

}
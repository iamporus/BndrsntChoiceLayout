package com.prush.bndrsntchoicelayout

import android.animation.ObjectAnimator
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.choice_layout.view.*

class BndrsntChoiceLayout : RelativeLayout {

    companion object {

        private const val DEFAULT_CHOICE_COLOR = android.R.color.darker_gray
        private const val SELECTED_CHOICE_COLOR = android.R.color.white
    }

    private var numberOfChoices: Int = 0
    private var choiceOneText: String? = null
    private var choiceTwoText: String? = null
    private var choiceThreeText: String? = null
    private var bRevealMode: Boolean = false

    @ColorInt
    private var defaultChoiceColor: Int = 0

    @ColorInt
    private var selectedChoiceColor: Int = 0

    private var onChoiceSelectedListener: OnChoiceSelectedListener? = null

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

    interface OnChoiceSelectedListener {

        fun onChoiceSelected(id: Int, choiceText: String)
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

        if (attributeSet != null) {

            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BndrsntChoiceLayout, 0, defStyle)

            numberOfChoices = typedArray.getInt(R.styleable.BndrsntChoiceLayout_no_of_choices, 0)

            choiceOneText = typedArray.getString(R.styleable.BndrsntChoiceLayout_choice_one_text)
            choiceTwoText = typedArray.getString(R.styleable.BndrsntChoiceLayout_choice_two_text)
            choiceThreeText = typedArray.getString(R.styleable.BndrsntChoiceLayout_choice_three_text)

            defaultChoiceColor =
                typedArray.getColor(R.styleable.BndrsntChoiceLayout_default_choice_color, 0)

            if (defaultChoiceColor == 0) {
                defaultChoiceColor = ContextCompat.getColor(context, DEFAULT_CHOICE_COLOR)
            }

            selectedChoiceColor =
                typedArray.getColor(R.styleable.BndrsntChoiceLayout_selected_choice_color, 0)

            if (selectedChoiceColor == 0) {
                selectedChoiceColor = ContextCompat.getColor(context, SELECTED_CHOICE_COLOR)
            }
            bRevealMode = typedArray.getBoolean(R.styleable.BndrsntChoiceLayout_reveal_mode, false)

            typedArray.recycle()

            when (numberOfChoices) {
                1 -> {
                    choiceLayout.visibility = View.GONE
                    choiceSingleTextView.text = choiceOneText
                    choiceSingleTextView.setOnClickListener {
                        onChoiceSelected(R.id.choiceSingleTextView, choiceOneText ?: "")
                    }
                }
                2 -> {
                    choiceLayout.visibility = View.VISIBLE
                    choiceSingleTextView.visibility = View.GONE

                    choiceOneTextView.text = choiceOneText
                    choiceTwoTextView.text = choiceTwoText

                    choiceOneTextView.setOnClickListener {
                        onChoiceSelected(R.id.choiceOneTextView, choiceOneText ?: "")
                    }

                    choiceTwoTextView.setOnClickListener {
                        onChoiceSelected(R.id.choiceTwoTextView, choiceTwoText ?: "")
                    }
                }
                3 -> {
                    choiceLayout.visibility = View.VISIBLE
                    choiceSingleTextView.visibility = View.VISIBLE

                    choiceOneTextView.text = choiceOneText
                    choiceTwoTextView.text = choiceTwoText
                    choiceSingleTextView.text = choiceThreeText

                    choiceOneTextView.setOnClickListener {
                        onChoiceSelected(R.id.choiceOneTextView, choiceOneText ?: "")
                    }

                    choiceTwoTextView.setOnClickListener {
                        onChoiceSelected(R.id.choiceTwoTextView, choiceTwoText ?: "")
                    }

                    choiceSingleTextView.setOnClickListener {
                        onChoiceSelected(R.id.choiceSingleTextView, choiceThreeText ?: "")
                    }
                }
            }

            // keep the view hidden in the beginning
            alpha = if (bRevealMode) 0f else 1f

            choiceOneTextView.setTextColor(defaultChoiceColor)
            choiceTwoTextView.setTextColor(defaultChoiceColor)
            choiceSingleTextView.setTextColor(defaultChoiceColor)
        }
    }

    private fun onChoiceSelected(id: Int, choiceText: String) {

        onChoiceSelectedListener?.onChoiceSelected(id, choiceText)

        bndrSntchTimer.reset()

        when(id)
        {
            R.id.choiceOneTextView -> {
                choiceOneTextView.setTextColor(selectedChoiceColor)
            }
            R.id.choiceTwoTextView -> {
                choiceTwoTextView.setTextColor(selectedChoiceColor)
            }
            R.id.choiceSingleTextView -> {
                choiceSingleTextView.setTextColor(selectedChoiceColor)
            }
        }

        //cleanup
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
        bndrSntchTimer.start(duration) { elapsedDuration, totalDuration ->

            if (elapsedDuration >= totalDuration) {
                onTimerElapsedListener.onTimerElapsed()
            }
        }
    }

    public fun setOnChoiceSelectedListener(onChoiceSelectedListener: OnChoiceSelectedListener) {
        this.onChoiceSelectedListener = onChoiceSelectedListener
    }


    fun getLifeCycleObserver(): LifecycleObserver {
        return bndrSntchTimer.lifecycleObserver
    }

}
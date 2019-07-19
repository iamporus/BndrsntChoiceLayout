/*
 * Copyright 2019 Purushottam Pawar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import java.util.*

class BndrsntChoiceLayout : RelativeLayout, View.OnClickListener {

    companion object {

        private const val DEFAULT_CHOICE_COLOR = android.R.color.darker_gray
        private const val SELECTED_CHOICE_COLOR = android.R.color.white
    }

    var numberOfChoices: Int = 0
        set(value) {
            field = value
            initLayout()
        }

    var choiceOneText: String? = null
        set(value) {
            field = value
            choiceOneTextView.text = value
        }

    var choiceTwoText: String? = null
        set(value) {
            field = value
            choiceTwoTextView.text = value
        }

    var choiceThreeText: String? = null
        set(value) {
            field = value
            choiceSingleTextView.text = value
        }

    var bRevealMode: Boolean = false
        set(revealMode) {
            field = revealMode
            alpha = if (revealMode) 0f else 1f
        }

    var bRandomizeChoice: Boolean = false

    @ColorInt
    var defaultChoiceColor: Int = 0
        set(value) {
            field = value
            choiceOneTextView.setTextColor(value)
            choiceTwoTextView.setTextColor(value)
            choiceSingleTextView.setTextColor(value)
        }

    @ColorInt
    var selectedChoiceColor: Int = 0

    var onChoiceSelectedListener: OnChoiceSelectedListener? = null

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
            bRandomizeChoice = typedArray.getBoolean(R.styleable.BndrsntChoiceLayout_randomize_choice, false)

            typedArray.recycle()

        }

        initLayout()

    }

    private fun initLayout() {

        // keep the view hidden in the beginning
        alpha = if (bRevealMode) 0f else 1f

        when (numberOfChoices) {
            1 -> {
                choiceLayout.visibility = View.GONE
                choiceSingleTextView.text = choiceOneText
            }
            2 -> {
                choiceSingleTextView.visibility = View.GONE

                choiceLayout.visibility = View.VISIBLE
                choiceOneTextView.text = choiceOneText
                choiceTwoTextView.text = choiceTwoText
            }
            3 -> {
                choiceLayout.visibility = View.VISIBLE
                choiceSingleTextView.visibility = View.VISIBLE

                choiceOneTextView.text = choiceOneText
                choiceTwoTextView.text = choiceTwoText
                choiceSingleTextView.text = choiceThreeText

            }
        }

        choiceOneTextView.setOnClickListener(this)
        choiceTwoTextView.setOnClickListener(this)
        choiceSingleTextView.setOnClickListener(this)


        choiceOneTextView.setTextColor(defaultChoiceColor)
        choiceTwoTextView.setTextColor(defaultChoiceColor)
        choiceSingleTextView.setTextColor(defaultChoiceColor)
    }

    override fun onClick(view: View?) {

        if (bndrSntchTimer.isRunning) {

            when (view?.id) {
                R.id.choiceOneTextView -> {
                    choiceOneTextView.setTextColor(selectedChoiceColor)
                    onChoiceSelectedListener?.onChoiceSelected(R.id.choiceOneTextView, choiceOneText ?: "")
                }
                R.id.choiceTwoTextView -> {
                    choiceTwoTextView.setTextColor(selectedChoiceColor)
                    onChoiceSelectedListener?.onChoiceSelected(R.id.choiceTwoTextView, choiceTwoText ?: "")
                }
                R.id.choiceSingleTextView -> {
                    choiceSingleTextView.setTextColor(selectedChoiceColor)
                    onChoiceSelectedListener?.onChoiceSelected(R.id.choiceSingleTextView, choiceThreeText ?: "")
                }
            }

            bndrSntchTimer.reset()
        }

    }

    fun startTimer(duration: Long, onTimerElapsedListener: OnTimerElapsedListener) {

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

            if (elapsedDuration >= totalDuration - 1000) {
                if (bRandomizeChoice) {
                    val random = Random()
                    when (random.nextInt(numberOfChoices) + 1) {
                        1 -> {
                            onClick(choiceOneTextView)
                        }
                        2 -> {
                            onClick(choiceTwoTextView)
                        }
                        3 -> {
                            onClick(choiceSingleTextView)
                        }
                    }
                } else if (elapsedDuration >= totalDuration) {
                    onTimerElapsedListener.onTimerElapsed()
                }
            }
        }
    }

    fun getLifeCycleObserver(): LifecycleObserver {
        return bndrSntchTimer.lifecycleObserver
    }

}
package com.prush.bndrsntchoicelayoutsample

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.prush.bndrsntchoicelayout.BndrsntChoiceLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bndrnstChoiceLayout.numberOfChoices = 3
        bndrnstChoiceLayout.choiceOneText = "Yes"
        bndrnstChoiceLayout.choiceTwoText = "No"
        bndrnstChoiceLayout.choiceThreeText = "Whatever"
        bndrnstChoiceLayout.bRandomizeChoice = true
        bndrnstChoiceLayout.bRevealMode = true
        bndrnstChoiceLayout.defaultChoiceColor = ContextCompat.getColor(applicationContext, android.R.color.holo_blue_light)

        if (savedInstanceState == null) {
            bndrnstChoiceLayout.postDelayed({

                bndrnstChoiceLayout.startTimer(10000, object : BndrsntChoiceLayout.OnTimerElapsedListener {
                    override fun onTimerElapsed() {
                        Log.d("MainActivity", "Finished")
                    }
                })

            }, 2000)
        }
        bndrnstChoiceLayout.onChoiceSelectedListener = (object : BndrsntChoiceLayout.OnChoiceSelectedListener {
            override fun onChoiceSelected(id: Int, choiceText: String) {
                Log.d("MainActivity", "selected: $choiceText")

            }

        })

        lifecycle.addObserver(bndrnstChoiceLayout.getLifeCycleObserver())


    }
}

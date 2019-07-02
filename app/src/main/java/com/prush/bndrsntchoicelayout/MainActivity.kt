package com.prush.bndrsntchoicelayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            bndrnstChoiceLayout.postDelayed({

                bndrnstChoiceLayout.startTimer(10000, object : BndrsntChoiceLayout.OnTimerElapsedListener {
                    override fun onTimerElapsed() {
                        Log.d("MainActivity", "Finished")
                    }
                })

            }, 2000)
        }
        bndrnstChoiceLayout.setOnChoiceSelectedListener(object : BndrsntChoiceLayout.OnChoiceSelectedListener {
            override fun onChoiceSelected(id: Int, choiceText: String) {
                Log.d("MainActivity", "selected: $choiceText")

            }

        })

        lifecycle.addObserver(bndrnstChoiceLayout.getLifeCycleObserver())


    }
}

package com.prush.bndrsntchoicelayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bndrnstChoiceLayout.postDelayed({

            bndrnstChoiceLayout.startTimer(10000, object : BndrsntChoiceLayout.OnTimerElapsedListener {
                override fun onTimerElapsed() {
                    Toast.makeText(applicationContext, "Finished", Toast.LENGTH_SHORT).show()
                }
            })

        }, 2000)

        bndrnstChoiceLayout.setOnChoiceSelectedListener(object : BndrsntChoiceLayout.OnChoiceSelectedListener {
            override fun onChoiceSelected(id: Int, choiceText: String) {
                Toast.makeText(applicationContext, "selected: $choiceText", Toast.LENGTH_SHORT).show()

            }

        })

        lifecycle.addObserver(bndrnstChoiceLayout.getLifeCycleObserver())

    }
}

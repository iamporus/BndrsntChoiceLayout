package com.prush.bndrsntchoicelayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bndrsntChoiceLayout: BndrsntChoiceLayout = findViewById(R.id.bndrnstChoiceLayout)

        bndrsntChoiceLayout.postDelayed({

            bndrsntChoiceLayout.startTimer(10000, object : BndrsntChoiceLayout.OnTimerElapsedListener {
                override fun onTimerElapsed() {
                    Toast.makeText(applicationContext, "Finished", Toast.LENGTH_SHORT).show()
                }
            })

        }, 2000)

        lifecycle.addObserver(bndrsntChoiceLayout.getLifeCycleObserver())

    }
}

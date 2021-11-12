package com.example.workmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.work.*


const val INPUT_DATA_KEY = "input data key"
const val SHARED_PREFERENCE_NAME = "sp name"
const val WORK_A = "work a"
const val WORK_B = "work b"

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        sp.registerOnSharedPreferenceChangeListener(this)

        findViewById<Button>(R.id.button).setOnClickListener {
            val requestA = createWork(WORK_A)
            val requestB = createWork(WORK_B)

            workManager.beginWith(requestA).then(requestB).enqueue()
        }
    }

    private fun createWork(name: String): OneTimeWorkRequest {
        val workConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(workConstraints)
            .setInputData(workDataOf(Pair(INPUT_DATA_KEY, name)))
            .build()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        updateTextView()
    }

    private fun updateTextView() {
        val sp = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        findViewById<TextView>(R.id.textView).text = sp.getInt(WORK_A, 0).toString()
        findViewById<TextView>(R.id.textView2).text = sp.getInt(WORK_B, 0).toString()
    }
}
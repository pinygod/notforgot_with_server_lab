package com.example.notforgot.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.notforgot.*
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.*
import com.example.notforgot.models.network.data.Category
import com.example.notforgot.models.network.data.Task
import com.example.notforgot.models.network.data.TaskForm
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var synchronizationLayout: ConstraintLayout
        private lateinit var synchronizationAnimation: LottieAnimationView

        fun enableSynchronizationAnimation() {
            synchronizationAnimation.progress = 0f
            synchronizationLayout.visibility = View.VISIBLE
            synchronizationAnimation.playAnimation()
        }

        fun disableSynchronizationAnimation() {
            synchronizationLayout.visibility = View.INVISIBLE
            synchronizationAnimation.pauseAnimation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainActivity.synchronizationLayout = synchronizationLayout
        MainActivity.synchronizationAnimation = synchronizationAnimation
    }

}
package com.summer.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        btn_profile_next.setOnClickListener {
            vf_profile_profile.showNext()
        }

        btn_profile_previous.setOnClickListener {
            vf_profile_profile.showPrevious()

        }


    }
}
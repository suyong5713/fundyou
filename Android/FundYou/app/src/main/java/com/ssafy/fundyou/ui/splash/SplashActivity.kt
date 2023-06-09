package com.ssafy.fundyou.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.fundyou.common.ViewState
import com.ssafy.fundyou.databinding.ActivitySplashBinding
import com.ssafy.fundyou.ui.MainActivity
import com.ssafy.fundyou.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSplashViewModel()
        tokenValidation()
    }

    private fun tokenValidation() {
        Handler(mainLooper).postDelayed({
            splashViewModel.getLocalAccessToken()
        }, 1000)
    }

    private fun initSplashViewModel() {
        splashViewModel.accessToken.observe(this) { response ->
            when (response) {
                is ViewState.Loading -> {

                }
                is ViewState.Success -> {
                    splashViewModel.getJWTByRefreshToken()
                }
                is ViewState.Error -> {
                    startLoginActivity()
                }
            }
        }

        splashViewModel.refreshJWT.observe(this) { response ->
            when (response) {
                is ViewState.Loading -> {

                }
                is ViewState.Success -> {
                    startMainActivity()
                }
                is ViewState.Error -> {
                    startLoginActivity()
                }
            }
        }
    }

    private fun startMainActivity() {
        // CLEAR_TASK or NEW_TASK로 스택에 있는 액티비티 전부 제거
        val mainiIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        //링크 공유로 접근했을 때 받을 Id값
        val itemId = intent.data?.getQueryParameter("item_id")
        val fundingId = intent.data?.getQueryParameter("funding_id")

        if(itemId != null){
            mainiIntent.putExtra("item_id", itemId)
        } else if( fundingId != null){
            mainiIntent.putExtra("funding_id", fundingId)
        }

        //FCM 푸쉬 알림 눌렀을 때 Intent처리
        var user = intent.getStringExtra("user")
        Log.d(TAG, "startMainActivity: user : ${user.toString()}")
        if(user == null){
            if(intent.extras?.getString("isHost").toString() == "true"){
                user = "host"
            } else if(intent.extras?.getString("isHost").toString() == "false"){
                user = "participate"
            }
            Log.d(TAG, "startMainActivity: ${intent.extras?.getString("isHost")}")
        }
        if(user != null){
            mainiIntent.putExtra("user", user)
        }
        startActivity(mainiIntent)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG = "SplashActivity..."
    }
}
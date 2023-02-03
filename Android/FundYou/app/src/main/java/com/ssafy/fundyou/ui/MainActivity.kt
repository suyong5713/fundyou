package com.ssafy.fundyou.ui

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ssafy.fundyou.R
import com.ssafy.fundyou.databinding.ActivityBridgeBinding
import com.ssafy.fundyou.databinding.ActivityMainBinding
import com.ssafy.fundyou.ui.home.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        initNavigation()
    }

    private fun initNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener { _, _, _ ->
            when (navController.currentDestination?.id) {
                R.id.mainFragment -> {
                    setToolbarType(ToolbarType.LOGO_WISH)
                    setBottomNavigationVisibility(View.VISIBLE)
                }
                R.id.searchFragment -> {
                    setToolbarType(ToolbarType.TEXT, "검색")
                    setBottomNavigationVisibility(View.VISIBLE)
                }
            }
        }
        binding.bnvMain.setupWithNavController(navController)
    }

    private fun setBottomNavigationVisibility(visibility: Int){
        binding.bnvMain.visibility = visibility
    }

    private fun setToolbarType(type : ToolbarType, title : String = ""){
        when(type){
            ToolbarType.LOGO_WISH -> {
                setToolbarLeft(ToolbarLeftImg.LOGO)
                setToolbarRight(ToolbarRightImg.WISH)
            }
            ToolbarType.BACK_TITLE_WISH -> {
                setToolbarTitle(title)
                setToolbarLeft(ToolbarLeftImg.BACK)
                setToolbarRight(ToolbarRightImg.WISH)
            }
            ToolbarType.TEXT -> {
                setToolbarTitle(title)
            }
            ToolbarType.TEXT_CANCEL -> {
                setToolbarTitle(title)
                setToolbarRight(ToolbarRightImg.CANCEL)
            }
            ToolbarType.BACK_TEXT -> {
                setToolbarLeft(ToolbarLeftImg.BACK)
                setToolbarTitle(title)
            }
        }
    }

    private fun setToolbarLeft(type : ToolbarLeftImg){
        when(type){
            ToolbarLeftImg.BACK -> {
                with(binding.lyToolbar.ivLeftImg){
                    setOnClickListener {
                        navController.popBackStack()
                    }
                    setImageResource(R.drawable.ic_back_arrow_no_line)
                }
            }
            ToolbarLeftImg.LOGO -> {
                binding.lyToolbar.ivLeftImg.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }

    private fun setToolbarRight(type : ToolbarRightImg){
        when(type){
            ToolbarRightImg.WISH -> {
                with(binding.lyToolbar.ivRightImg){
                    setImageResource(R.drawable.ic_wish_list)
                    setOnClickListener {

                    }
                }
            }
            ToolbarRightImg.CANCEL -> {
                with(binding.lyToolbar.ivLeftImg){
                    setImageResource(R.drawable.ic_cancel)
                    setOnClickListener {
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    private fun setToolbarTitle(title : String){
        binding.lyToolbar.tvTitle.text = title
    }

    companion object {
        private const val TAG = "MainActivity..."
    }
}

enum class ToolbarType{
    LOGO_WISH, BACK_TITLE_WISH, TEXT, TEXT_CANCEL, BACK_TEXT
}

enum class ToolbarLeftImg{
    LOGO, BACK
}

enum class ToolbarRightImg{
    WISH, CANCEL
}
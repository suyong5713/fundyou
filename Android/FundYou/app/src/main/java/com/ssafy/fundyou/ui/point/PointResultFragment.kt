package com.ssafy.fundyou.ui.point

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.ssafy.fundyou.R
import com.ssafy.fundyou.common.ViewState
import com.ssafy.fundyou.databinding.FragmentPointResultBinding
import com.ssafy.fundyou.ui.common.BaseFragment
import com.ssafy.fundyou.ui.pay.PayViewModel

class PointResultFragment :
    BaseFragment<FragmentPointResultBinding>(R.layout.fragment_point_result) {
    private val pointViewModel by activityViewModels<PointViewModel>()
    private val payViewModel by activityViewModels<PayViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModels()
    }

    override fun initView() {

        initBtnClickListener()
    }

    override fun initViewModels() {
        initResultObserve()
    }

    private fun initResultObserve() {
        pointViewModel.resultLoad.observe(viewLifecycleOwner) {
            val response = pointViewModel.resultLoad.value!!
            Log.d(TAG, "initResultObserve: ${response.value}")
            when (response) {
                is ViewState.Loading -> {
                    Log.d(TAG, "initResultObserve: Load Point Loading...")
                }
                is ViewState.Success -> {
                    Log.d(TAG, "initResultObserve: ${response.value.toString()}")
                    binding.tvPointResult.text = "충전이 완료되었습니다!\n친구들에게 뜻깊은 선물을 해보세요."
                }
                is ViewState.Error -> {
                    binding.tvPointResult.text = "충전에 실패했습니다. 다시 시도해주세요."
                    Log.d(TAG, "initResultObserve: Load Point Error...${response.message}")
                }
            }
        }
    }

    private fun initBtnClickListener() {
        when (pointViewModel.beforeFragment.value) {
            "mypage" -> {
                requireActivity().onBackPressedDispatcher.addCallback(this) {
                    navigate(PointResultFragmentDirections.actionPointResultFragmentToMyPageFragment())
                }
                binding.btnPointResult.setOnClickListener {
                    navigate(PointResultFragmentDirections.actionPointResultFragmentToMyPageFragment())
                }
            }
            "pay" -> {
                val fundingItemId = payViewModel.fundingItemId.value
                Log.d(TAG, "initBtnClickListener: $fundingItemId")
                requireActivity().onBackPressedDispatcher.addCallback(this) {
                    navigate(
                        PointResultFragmentDirections.actionPointResultFragmentToPayFragment(
                            fundingItemId ?: 0
                        )
                    )
                }

                binding.btnPointResult.setOnClickListener {

                    navigate(
                        PointResultFragmentDirections.actionPointResultFragmentToPayFragment(
                            fundingItemId ?: 0
                        )
                    )
                }
            }
        }
    }
}
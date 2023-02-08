package com.ssafy.fundyou.ui.funding_my

import android.os.Bundle
import android.view.View
import com.ssafy.fundyou.R
import com.ssafy.fundyou.databinding.FragmentMyFundingBinding
import com.ssafy.fundyou.ui.base.BaseFragment
import com.ssafy.fundyou.ui.funding_my.adapter.MyFundingEndListAdapter
import com.ssafy.fundyou.ui.funding_my.adapter.MyFundingProcessingListAdapter

class MyFundingFragment : BaseFragment<FragmentMyFundingBinding>(R.layout.fragment_my_funding) {

    private val processingFundingAdapter = MyFundingProcessingListAdapter()
    private val endFundingAdapter = MyFundingEndListAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModels()
    }

    override fun initView() {
        initRecyclerView()
        addMyFundingDetailEvent()
    }

    override fun initViewModels() {

    }

    private fun addMyFundingDetailEvent(){
        binding.tvFundingDetail.setOnClickListener {
            navigate(MyFundingFragmentDirections.actionMyFundingFragmentToFundingDetailFragment())
        }
    }

    private fun initRecyclerView(){
//        val tempList = listOf(
//            FundingItemModel(
//                id = 0,
//                ProductItemModel(id = 1, price = 3000, "img", "상품이름1", false, "브랜드1", true),
//                tempProductImg = R.drawable.ic_launcher_background,
//                currentFundingPrice = 1000,
//                fundingParticipate = 3,
//            ), FundingItemModel(
//                id = 1,
//                ProductItemModel(id = 2, price = 30000, "img", "상품이름2", false, "브랜드3", true),
//                tempProductImg = R.drawable.ic_launcher_background,
//                currentFundingPrice = 30000,
//                fundingParticipate = 5,
//            ),
//            FundingItemModel(
//                id = 2,
//                ProductItemModel(id = 2, price = 30000, "img", "상품이름2", false, "브랜드3", true),
//                tempProductImg = R.drawable.ic_launcher_background,
//                currentFundingPrice = 30000,
//                fundingParticipate = 5,
//            )
//        )
//        processingFundingAdapter.submitList(tempList)
//        endFundingAdapter.submitList(tempList)
//
//        binding.rvProgressingFundingList.adapter = processingFundingAdapter
//        binding.rvEndFundingList.adapter = endFundingAdapter
    }
}
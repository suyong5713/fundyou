package com.ssafy.fundyou.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fundyou.common.SingleLiveEvent
import com.ssafy.fundyou.common.ViewState
import com.ssafy.fundyou.domain.usecase.funding.SaveFundingInfoUseCase
import com.ssafy.fundyou.domain.usecase.item.GetRandomItemUseCase
import com.ssafy.fundyou.domain.usecase.item.GetRankingItemUseCase
import com.ssafy.fundyou.ui.home.model.RandomItemModel
import com.ssafy.fundyou.ui.home.model.RankingItemModel
import com.ssafy.fundyou.ui.home.model.toRandomItemModel
import com.ssafy.fundyou.ui.home.model.toRankingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.apache.commons.lang3.mutable.Mutable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRankingItemUserCase: GetRankingItemUseCase,
    private val getRandomItemUseCase: GetRandomItemUseCase,
    private val saveFundingInfoUseCase: SaveFundingInfoUseCase
) : ViewModel() {

    private val _rankingItemList = MutableLiveData<ViewState<List<RankingItemModel>>>()
    val rankingItemList: LiveData<ViewState<List<RankingItemModel>>>
        get() = _rankingItemList

    private val _randomItemList = MutableLiveData<ViewState<List<RandomItemModel>>>()
    val randomItemList: LiveData<ViewState<List<RandomItemModel>>>
        get() = _randomItemList

    private val _rankingCategoryId = MutableLiveData<Int>()
    val rankingCategoryId: LiveData<Int>
        get() = _rankingCategoryId

    private val _rankingMinPrice = MutableLiveData<Int>()
    val rankingMinPrice: LiveData<Int>
        get() = _rankingMinPrice

    private val _rankingMaxPrice = MutableLiveData<Int>()
    val rankingMaxPrice: LiveData<Int>
        get() = _rankingMaxPrice

    private val _savedFundingId = SingleLiveEvent<ViewState<Long>>()
    val savedFundingId: LiveData<ViewState<Long>> get() = _savedFundingId

    fun saveFundingInfo(fundingId: Long) = viewModelScope.launch {
        _savedFundingId.postValue(ViewState.Loading())
        try {
            val response = saveFundingInfoUseCase(fundingId)
            _savedFundingId.postValue(ViewState.Success(response))
        } catch (e: Exception) {
            _savedFundingId.postValue(ViewState.Error(e.message))
        }
    }

    fun getRankingItemList(categoryId: Int, minPrice: Int, maxPrice: Int) = viewModelScope.launch {
        _rankingItemList.value = ViewState.Loading()
        try {
            val response = getRankingItemUserCase(categoryId, minPrice, maxPrice)
            _rankingItemList.value = ViewState.Success(response.map { it.toRankingModel() })
            val itemlist = _rankingItemList.value?.value
            Log.d(TAG, "getRankingItemList: ${itemlist}")
        } catch (e: Exception) {
            _rankingItemList.value = ViewState.Error(e.message)
        }
    }

    fun getRandomItemList() = viewModelScope.launch {
        _randomItemList.value = ViewState.Loading()
        try {
            val response = getRandomItemUseCase()
            _randomItemList.value = ViewState.Success(response.map { it.toRandomItemModel() })
        } catch (e: Exception) {
            _randomItemList.value = ViewState.Error(e.message)
        }
    }

    fun setCategory(categoryId: Int) {
        _rankingCategoryId.value = categoryId
    }

    fun setPrice(min: Int, max: Int) {
        _rankingMinPrice.value = min
        _rankingMaxPrice.value = max
    }
}
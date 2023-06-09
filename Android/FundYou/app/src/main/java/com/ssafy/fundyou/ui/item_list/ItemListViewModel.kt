package com.ssafy.fundyou.ui.item_list

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fundyou.common.ViewState
import com.ssafy.fundyou.domain.usecase.item.AddLikeItemUseCase
import com.ssafy.fundyou.domain.usecase.item.GetAllProductItemUseCase
import com.ssafy.fundyou.domain.usecase.item.GetCategoryItemUseCase
import com.ssafy.fundyou.domain.usecase.item.GetItemByPriceUseCase
import com.ssafy.fundyou.ui.item_list.model.ItemListModel
import com.ssafy.fundyou.ui.item_list.model.toItemListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val getAllProductItemUseCase: GetAllProductItemUseCase,
    private val getCategoryItemUseCase: GetCategoryItemUseCase,
    private val getItemByPriceUseCase: GetItemByPriceUseCase,
    private val addLikeItemUseCase: AddLikeItemUseCase
) : ViewModel() {
    private val _itemList = MutableLiveData<ViewState<List<ItemListModel>>>()
    val itemList: LiveData<ViewState<List<ItemListModel>>>
        get() = _itemList

    private val _categoryId = MutableLiveData<Int>()
    val categoryId: LiveData<Int>
        get() = _categoryId

    private val _minPrice = MutableLiveData<Int>()
    val minPrice: LiveData<Int>
        get() = _minPrice

    private val _maxPrice = MutableLiveData<Int>()
    val maxPrice: LiveData<Int>
        get() = _maxPrice

    private val _sumPrice = MutableLiveData<Int>()
    val sumPrice: LiveData<Int>
        get() = _sumPrice

    fun getAllItemList() = viewModelScope.launch {
        _itemList.value = ViewState.Loading()
        try {
            val response = getAllProductItemUseCase()
            _itemList.value = ViewState.Success(response.map { it.toItemListModel() })
            Log.d(TAG, "getAllItemList: ${itemList.value}")
        } catch (e: Exception) {
            _itemList.value = ViewState.Error(e.message)
        }
    }

    fun getCategoryItemList(categoryId: Int) = viewModelScope.launch {
        _itemList.value = ViewState.Loading()
        try {
            val response = getCategoryItemUseCase(categoryId)
            _itemList.value = ViewState.Success(response.map { it.toItemListModel() })
        } catch (e: Exception) {
            _itemList.value = ViewState.Error(e.message)
        }
    }

    fun getItemByPrice(categoryId: Int, minPrice: Int, maxPrice: Int) = viewModelScope.launch {
        _itemList.value = ViewState.Loading()
        try {
            val response = getItemByPriceUseCase(categoryId, minPrice, maxPrice)
            _itemList.value = ViewState.Success(response.map { it.toItemListModel() })
        } catch (e: Exception) {
            _itemList.value = ViewState.Error(e.message)
        }
    }

    fun setCategory(categoryId: Int) {
        _categoryId.value = categoryId
    }

    fun setPrice(min: Int, max: Int) {
        _minPrice.value = min
        _maxPrice.value = max
        _sumPrice.value = min + max
    }

    fun addLikeItem(id: Long) = viewModelScope.launch {
        addLikeItemUseCase(id)
        getAllItemList()
    }
}
package com.ssafy.fundyou.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.fundyou.R
import com.ssafy.fundyou.databinding.ItemListProductBinding
import com.ssafy.fundyou.domain.model.ProductItemModel

class ProductItemAdapter : ListAdapter<ProductItemModel, ProductItemAdapter.ProductItemViewHolder>(
    ProductDiffUtil()
) {
    private var needRanking = false
    private var isFavoriteFragment = false
    inner class ProductItemViewHolder(val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductItemModel, position: Int) {
            binding.product = item

            if(needRanking){
                with(binding.tvItemListProductRanking){
                    visibility = View.VISIBLE
                    text = "${position + 1}위"
                }
            }
            if(isFavoriteFragment){
                binding.isFavoriteFragment = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = DataBindingUtil.inflate<ItemListProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_list_product, parent, false
        )
        return ProductItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun checkNeedRanking(value: Boolean){
        needRanking = value
    }
    fun setIsFavoriteFragment(value: Boolean){
        isFavoriteFragment = value
    }
    class ProductDiffUtil : DiffUtil.ItemCallback<ProductItemModel>() {
        /**
         * 두 아이템의 내부 데이터가 동일한지 확인
         * return value가 true면 areContentsTheSame을 실행해 두 값이 동일한지 확인
         * */
        override fun areItemsTheSame(
            oldItem: ProductItemModel,
            newItem: ProductItemModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        /** 두 아이템의 값이 동일한지 확인 */
        override fun areContentsTheSame(
            oldItem: ProductItemModel,
            newItem: ProductItemModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
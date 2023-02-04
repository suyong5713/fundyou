package com.ssafy.fundyou.ui.arcore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.ssafy.fundyou.R
import com.ssafy.fundyou.databinding.FragmentGalleryDetailBinding
import com.ssafy.fundyou.ui.base.BaseFragment

class GalleryDetailFragment : BaseFragment<FragmentGalleryDetailBinding>(R.layout.fragment_gallery_detail) {

    val storage = FirebaseStorage.getInstance("gs://fundyou-1674632553418.appspot.com/")
    val storageRef = storage.reference
    val args: GalleryDetailFragmentArgs by navArgs()

    override fun initView() {
        val url = (args.arg1)
        storageRef.child(url).downloadUrl.addOnSuccessListener {
            Log.d("suyong", "download success!!")
            Glide.with(requireContext()).load(it).into(binding.imgArDetail)
        }.addOnFailureListener {
            Log.d("suyong", "test: download failed!")
        }
    }

    override fun initViewModels() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
}
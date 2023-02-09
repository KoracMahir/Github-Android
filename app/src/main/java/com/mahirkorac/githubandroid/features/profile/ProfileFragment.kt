package com.mahirkorac.githubandroid.features.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mahirkorac.githubandroid.R
import com.mahirkorac.githubandroid.databinding.FragmentProfileBinding
import com.mahirkorac.githubandroid.features.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        viewModel.getCurrentUserProfile(getOAuthAccessToken())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) {
            it.data.let { currentUser ->
                Glide.with(requireContext())
                    .load(currentUser?.avatar_url)
                    .into(binding.avatarImage)
                binding.userLogin.text = currentUser?.login
                binding.followersCount.text = currentUser?.followers.toString()
                binding.followingCount.text = currentUser?.following.toString()
                binding.memberDate.text = currentUser?.created_at
                binding.lastInteractionDate.text = currentUser?.updated_at
            }
            binding.userData.isVisible = !it.loading
            binding.progressBar.isVisible = it.loading
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getOAuthAccessToken(): String {
        val sh: SharedPreferences? = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        return sh?.getString("accessToken", "") ?: ""
    }
}
package com.mahirkorac.githubandroid.features.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mahirkorac.githubandroid.databinding.FragmentRepositoryDetailsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RepositoryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRepositoryDetailsBinding
    private val args: RepositoryDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepositoryDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repositoryDetails = args.repositoryDetails

        Glide.with(requireContext())
            .load(repositoryDetails.owner?.avatar_url)
            .into(binding.ownerImage)
        binding.ownerName.text = repositoryDetails.owner?.login
        binding.repositoryName.text = repositoryDetails.full_name
        binding.watchersCount.text = "Watchers: ${repositoryDetails.watchers_count}"
        binding.forksCount.text = "Forks: ${repositoryDetails.forks_count}"
        binding.issuesCount.text = "Issues: ${repositoryDetails.open_issues_count}"
        binding.starsCount.text = repositoryDetails.stargazers_count.toString()
        binding.description.text = repositoryDetails.description

        binding.detailsTitle.setOnClickListener { findNavController().popBackStack() }

        binding.ownerImage.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/${repositoryDetails.owner?.login}"))
            startActivity(browserIntent)
        }

        binding.repositoryName.setOnClickListener {
            openRepository(
                repositoryDetails.full_name
            )
        }
    }

    private fun openRepository(repositoryName: String?) {
        val browserIntent =
            Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://github.com/${repositoryName}"
                )
            )
        startActivity(browserIntent)
    }

}
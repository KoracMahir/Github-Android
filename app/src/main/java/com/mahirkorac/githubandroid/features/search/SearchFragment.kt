package com.mahirkorac.githubandroid.features.search

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.textChanges
import com.mahirkorac.githubandroid.BuildConfig
import com.mahirkorac.githubandroid.adapter.SearchAdapter
import com.mahirkorac.githubandroid.databinding.FragmentSearchBinding
import com.mahirkorac.githubandroid.features.filter.FilterBottomSheetFragment
import com.mahirkorac.githubandroid.features.search.model.AccessToken
import com.mahirkorac.githubandroid.features.search.viewmodel.SearchViewModel
import com.mahirkorac.githubandroid.utils.Constants.clientID
import com.mahirkorac.githubandroid.utils.Constants.oauthLoginURL
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private var searchAdapter: SearchAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        sharedPreferences = context?.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        layoutManager = LinearLayoutManager(context)
        binding.repositoryRecycler.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.textChanges()
            .skipInitialValue()
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribe {
                if (viewModel.queryValue.value != it.toString() && it.toString().isNotEmpty())
                    viewModel.updateQuery(it.toString())
            }

        viewModel.repositories.observe(viewLifecycleOwner) {
            searchAdapter = it.data?.let { repositories ->
                SearchAdapter(repositories.items)
            } ?: run {
                SearchAdapter(listOf())
            }
            it.message?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            binding.repositoryRecycler.adapter = searchAdapter
            searchAdapter?.setOnItemClickListener {
                if (!freePlanDetailsExceeds())
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToRepositoryDetailsFragment(it)
                    )
                else
                    Toast.makeText(
                        context, "Free trail is over! Please move on paid version of app", Toast
                            .LENGTH_LONG
                    ).show()
            }
        }

        viewModel.clearData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = !it
            binding.loadMore.isVisible = it
        }

        viewModel.queryValue.observe(viewLifecycleOwner) {
            viewModel.getSearchRepositories()
        }

        binding.searchCancel.setOnClickListener {
            binding.searchEditText.text.clear()
            viewModel.clearSearch()
            viewModel.updateFilter(null, null)
        }

        binding.filter.setOnClickListener {
            showBottomSheet()
        }

        viewModel.orderValue.observe(requireActivity()) {
            viewModel.getSearchRepositories()
        }

        viewModel.accessToken.observe(viewLifecycleOwner) {
            saveOAuthAccessToken(it)
        }

        binding.profile.setOnClickListener {
            if (BuildConfig.FLAVOR == PAID) {
                if (getOAuthAccessToken().isNullOrEmpty())
                    processLogin()
                else
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToProfileFragment()
                    )
            } else {
                Toast.makeText(
                    context, "Free trail is over! Please move on paid version of app", Toast
                        .LENGTH_LONG
                ).show()
            }
        }

        binding.loadMore.setOnClickListener {
            viewModel.loadMore()
        }

        viewModel.page.observe(viewLifecycleOwner) {
            viewModel.getSearchRepositories()
        }

    }

    override fun onResume() {
        super.onResume()
        val uri: Uri? = activity?.intent?.data
        if (uri != null) {
            uri.getQueryParameter("code")?.let { code ->
                viewModel.getAccessToken(code)
                Toast.makeText(context, "Login success!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Could not login, something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBottomSheet() {
        FilterBottomSheetFragment(viewModel).show(parentFragmentManager, "TAG")
    }

    private fun processLogin() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "$oauthLoginURL?client_id=$clientID&scope=repo"
            )
        )

        startActivity(intent)
    }

    private fun saveOAuthAccessToken(accessToken: AccessToken) {
        val myEdit = sharedPreferences?.edit()
        myEdit?.putString("accessToken", accessToken.accessToken)
        myEdit?.apply()
    }

    private fun getOAuthAccessToken(): String {
        return sharedPreferences?.getString("accessToken", "") ?: ""
    }

    private fun freePlanDetailsExceeds(): Boolean {
        var currentViewCount = sharedPreferences?.getInt("totalDetailPageViews", 0) ?: 0
        if (BuildConfig.FLAVOR == FREE) {
            val myEdit = sharedPreferences?.edit()
            myEdit?.putInt("totalDetailPageViews", ++currentViewCount)
            myEdit?.apply()
            if (currentViewCount >= BuildConfig.TOTAL_DETAIL_SCREENS)
                return true
        }
        return false
    }

    companion object {
        const val FREE = "free"
        const val PAID = "paid"
    }

}

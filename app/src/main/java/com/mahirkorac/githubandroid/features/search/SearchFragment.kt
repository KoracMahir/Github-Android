package com.mahirkorac.githubandroid.features.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.textChanges
import com.mahirkorac.githubandroid.adapter.SearchAdapter
import com.mahirkorac.githubandroid.databinding.FragmentSearchBinding
import com.mahirkorac.githubandroid.features.filter.FilterBottomSheetFragment
import com.mahirkorac.githubandroid.features.filter.model.OrderType
import com.mahirkorac.githubandroid.features.filter.model.SortType
import com.mahirkorac.githubandroid.features.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private var searchAdapter: SearchAdapter? = null
    private var layoutManager : RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
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
                if (viewModel.queryValue.value != it.toString())
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
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToRepositoryDetailsFragment
                    (it))
            }
        }

        viewModel.queryValue.observe(viewLifecycleOwner) {
            viewModel.getSearchRepositories()
        }

        binding.searchCancel.setOnClickListener {
            binding.searchEditText.text.clear()
            viewModel.clearSearch()
        }

        binding.filter.setOnClickListener {
            showBottomSheet()
        }

        viewModel.orderValue.observe(requireActivity()) {
            viewModel.getSearchRepositories()
        }

    }

    private fun showBottomSheet() {
        FilterBottomSheetFragment(viewModel).show(parentFragmentManager, "TAG")
    }

}

package com.mahirkorac.githubandroid.features.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahirkorac.githubandroid.adapter.SearchAdapter
import com.mahirkorac.githubandroid.databinding.FragmentSearchBinding
import com.mahirkorac.githubandroid.features.filter.FilterBottomSheetFragment
import com.mahirkorac.githubandroid.features.filter.model.OrderType
import com.mahirkorac.githubandroid.features.filter.model.SortType
import com.mahirkorac.githubandroid.features.search.model.SearchPayload
import com.mahirkorac.githubandroid.features.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), FragmentResultListener {

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        binding.repositoryRecycler.layoutManager = layoutManager

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateQuery(s.toString())
            }
        })

        viewModel.repositories.observe(viewLifecycleOwner) {
            it.data?.let { repositories ->
                searchAdapter = SearchAdapter(repositories.items)
                binding.repositoryRecycler.adapter = searchAdapter
            } ?: run {
                binding.repositoryRecycler.adapter = SearchAdapter(listOf())
            }
            it.message?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.queryValue.observe(viewLifecycleOwner){
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

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            "filterResults" -> {
                val sort = result.getString("SORT") ?: ""
                val order = result.getString("ORDER") ?: ""
                viewModel.updateFilter(
                    SortType.valueOf(sort),
                    OrderType.valueOf(order)
                )
            }
        }

    }

}

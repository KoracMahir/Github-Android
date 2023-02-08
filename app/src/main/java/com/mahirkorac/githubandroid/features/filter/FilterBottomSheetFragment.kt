package com.mahirkorac.githubandroid.features.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mahirkorac.githubandroid.databinding.FragmentFilterBottomSheetBinding
import com.mahirkorac.githubandroid.features.filter.model.OrderType
import com.mahirkorac.githubandroid.features.filter.model.SortType
import com.mahirkorac.githubandroid.features.search.viewmodel.SearchViewModel

class FilterBottomSheetFragment(private val viewModel: SearchViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.mostStars.setOnClickListener { updateFilter(SortType.STARTS, OrderType.DESC) }
        binding.fewestStars.setOnClickListener { updateFilter(SortType.STARTS, OrderType.ASC) }
        binding.mostForks.setOnClickListener { updateFilter(SortType.FORKS, OrderType.DESC) }
        binding.fewestForks.setOnClickListener { updateFilter(SortType.FORKS, OrderType.ASC) }
        binding.recentlyUdapted.setOnClickListener { updateFilter(SortType.UPDATED, OrderType.DESC) }
        binding.leastRecentlyUdapted.setOnClickListener { updateFilter(SortType.UPDATED, OrderType.ASC) }

    }

    private fun updateFilter(sortType: SortType, orderType: OrderType){
        viewModel.updateFilter(sortType, orderType)
        dismiss()
    }
}
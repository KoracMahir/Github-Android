package com.mahirkorac.githubandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahirkorac.githubandroid.databinding.RepositoryItemBinding
import com.mahirkorac.githubandroid.features.search.model.Item

class SearchAdapter(private val repositories: List<Item>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RepositoryItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount() = repositories.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        with(holder){
            with(repositories[position]){
                binding.repositoryName.text = this.name
                Glide.with(holder.itemView.context)
                    .load(this.owner.avatar_url)
                    .into(binding.ownerImage)
                binding.ownerName.text = this.owner.login
                binding.watchersCount.text = "Watchers: ${this.watchers_count}"
                binding.forksCount.text = "Forks: ${this.forks_count}"
                binding.issuesCount.text = "Issues: ${this.open_issues_count}"
                binding.starsCount.text = this.stargazers_count.toString()
                binding.repoUpdateDate.text = this.updated_at
            }
        }
    }

    inner class SearchViewHolder(val binding: RepositoryItemBinding) : RecyclerView.ViewHolder(binding.root)

}
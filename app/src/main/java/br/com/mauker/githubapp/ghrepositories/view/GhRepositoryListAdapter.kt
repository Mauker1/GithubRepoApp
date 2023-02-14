package br.com.mauker.githubapp.ghrepositories.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.mauker.githubapp.R
import br.com.mauker.githubapp.databinding.ItemGhRepositoryBinding
import br.com.mauker.githubapp.ghrepositories.domain.entity.GhRepositoryItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

class GhRepositoryListAdapter: PagingDataAdapter<GhRepositoryItem, GhRepositoryListAdapter.GhRepositoriesViewHolder>(RepoDiffCallback()) {

    class GhRepositoriesViewHolder(
        private val binding: ItemGhRepositoryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GhRepositoryItem) {
            binding.run {
                val context = binding.root.context
                txtRepoName.text = item.name
                txtRepoTitle.text = item.title
                txtRepoDescription.text = item.description
                txtRepoURL.text = context.getString(R.string.repo_url_placeholder, item.url)

                txtUserName.text = item.owner.name

                Glide.with(context)
                    .load(item.owner.avatarUrl)
                    .placeholder(R.drawable.img_github)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgProfile)

                btnGoToURL.setOnClickListener {
                    try {
                        val uri = Uri.parse(item.url)
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } catch (e: ActivityNotFoundException) {
                        Timber.e(e)
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_intent_browser),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    class RepoDiffCallback: DiffUtil.ItemCallback<GhRepositoryItem>() {
        override fun areItemsTheSame(oldItem: GhRepositoryItem, newItem: GhRepositoryItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GhRepositoryItem, newItem: GhRepositoryItem) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: GhRepositoriesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GhRepositoriesViewHolder (
            ItemGhRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}
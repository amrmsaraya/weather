package com.github.amrmsaraya.weather.presenter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.databinding.FavoritesItemBinding

class FavoritesAdapter(
    private val itemClickListener: (Location) -> Unit,
    private val deleteClickListener: (Location) -> Unit
) :
    ListAdapter<Location, FavoritesAdapter.FavoritesViewHolder>(
        FavoritesDiffUtil
    ) {
    inner class FavoritesViewHolder(val binding: FavoritesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener(getItem(adapterPosition))

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.favorites_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {

        holder.binding.tvFavoritesName.text = getItem(position).name
        holder.binding.btnMenu.setOnClickListener {
            val popupMenu = PopupMenu(holder.binding.root.context, it)
            popupMenu.inflate(R.menu.favorite_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.favMenuDelete -> deleteClickListener(getItem(position))
                }
                true
            }
            popupMenu.show()
        }
    }
}

object FavoritesDiffUtil : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }

}

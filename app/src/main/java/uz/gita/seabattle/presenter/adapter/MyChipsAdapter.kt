package uz.gita.seabattle.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.seabattle.data.model.KatakData
import androidx.core.graphics.toColorInt
import uz.gita.seabattle.databinding.ItemKatakBinding

class MyChipsAdapter: ListAdapter<KatakData, MyChipsAdapter.ChipViewHolder>(CellDiffCallback) {

    private var onItemCick: ((Int) -> Unit)? = null
    fun setOnItemClickListener(l: (Int)-> Unit){
        onItemCick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        return ChipViewHolder(ItemKatakBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChipViewHolder(private val binding: ItemKatakBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: KatakData) {
            if (item.hasShip) {
                binding.cellView.setBackgroundColor("#A680FF".toColorInt())
            } else {
                binding.cellView.setBackgroundColor("#746e99".toColorInt())
            }

            binding.root.setOnClickListener { onItemCick?.invoke(item.id) }
        }
    }

    object CellDiffCallback : DiffUtil.ItemCallback<KatakData>() {
        override fun areItemsTheSame(oldItem: KatakData, newItem: KatakData) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: KatakData, newItem: KatakData) = oldItem == newItem
    }
}
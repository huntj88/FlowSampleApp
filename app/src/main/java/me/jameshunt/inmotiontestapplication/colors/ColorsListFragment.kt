package me.jameshunt.inmotiontestapplication.colors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recycler.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class ColorsListFragment : FlowFragment<List<Color>, Color>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun flowWillRun(input: List<Color>) {
        updateAdapter(input)
    }

    private fun updateAdapter(newColors: List<Color>) {
        this.recyclerView.adapter
            ?.let { it as? ColorsAdapter }
            ?.let {
                if(newColors != it.colors) {
                    diffItems(newColors)
                }
            } ?: run { this.recyclerView.adapter = ColorsAdapter(newColors) }
    }

    private fun diffItems(newColors: List<Color>) {
        val adapter = this.recyclerView.adapter as ColorsAdapter

        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = adapter.colors.size
            override fun getNewListSize(): Int = newColors.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return adapter.colors[oldItemPosition] == newColors[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItemsTheSame(oldItemPosition, newItemPosition)
            }

        }
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(adapter)
        adapter.colors = newColors
    }

    private inner class ColorsAdapter(var colors: List<Color>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val colorView = View(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 200)
            }

            return object : RecyclerView.ViewHolder(colorView) {}
        }

        override fun getItemCount(): Int = colors.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val color = colors[position]
            val backgroundColor = color.let {
                android.graphics.Color.rgb(it.red, it.green, it.blue)
            }

            holder.itemView.setBackgroundColor(backgroundColor)

            holder.itemView.setOnClickListener {
                this@ColorsListFragment.resolve(color)
            }
        }
    }
}
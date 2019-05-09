package me.jameshunt.inmotiontestapplication.colors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recycler.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class ColorsListFragment : FlowFragment<Colors, Color>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun flowWillRun(input: Colors) {
        updateAdapter(input)
    }

    private fun updateAdapter(colors: Colors) {
        this.recyclerView.adapter
            ?.let {
                // diff needed when resuming existing fragment,
                // or when flow controller changes fragment data without showing a different fragment first
                TODO() }
            ?: run { this.recyclerView.adapter = ColorsAdapter(colors) }
    }

    private inner class ColorsAdapter(val colors: Colors) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val colorView = View(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 200)
            }

            return object : RecyclerView.ViewHolder(colorView) {}
        }

        override fun getItemCount(): Int = colors.colors.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val color = colors.colors[position]
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
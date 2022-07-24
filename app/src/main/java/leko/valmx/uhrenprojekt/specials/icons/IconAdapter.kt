package leko.valmx.uhrenprojekt.specials.icons

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_icon.view.*
import leko.valmx.uhrenprojekt.R

class IconAdapter(private val data: ArrayList<Int>, val context: Context)
    : RecyclerView.Adapter<IconAdapter.VH>() {

    class VH(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon, parent, false)
        return VH(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val content = data[position]
        holder.itemView.icon.background = context.resources.getDrawable(content)
    }

    override fun getItemCount(): Int = data.size
}
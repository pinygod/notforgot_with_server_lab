package com.example.notforgot.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notforgot.utils.Constants
import com.example.notforgot.R
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.data.Category
import com.example.notforgot.models.network.data.Task


class MainRecyclerAdapter(
    private var items: ArrayList<RecyclerObject>,
    var context: Context,
    private var listener: ItemListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class TitleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.categoryTitle) as TextView
    }

    class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.title) as TextView
        var description: TextView = itemView.findViewById<View>(R.id.description) as TextView
        var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        var cardView: CardView = itemView.findViewById(R.id.noteCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.TYPE_TITLE) {
            TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.note_category, parent, false)
            )
        } else {
            NoteViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == Constants.TYPE_TITLE) {
            (holder as TitleViewHolder).title.text =
                (items[position].item as Category).name

        } else {
            val item = (items[position].item as Task)
            (holder as NoteViewHolder).title.text = item.title
            holder.description.text = item.description
            holder.checkBox.isChecked = item.done == 1

            holder.cardView.setCardBackgroundColor(
                Color.parseColor(item.priority.color)
            )
            holder.checkBox.setOnClickListener {
                listener.onNoteStateChanged(item, position)
            }
            holder.itemView.setOnClickListener {
                listener.onNoteClicked(item, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    fun updateList(list: ArrayList<RecyclerObject>) {
        items = list
        this.notifyDataSetChanged()
    }

    interface ItemListener {
        fun onNoteStateChanged(item: Task, position: Int)
        fun onNoteClicked(item: Task, position: Int)
    }
}
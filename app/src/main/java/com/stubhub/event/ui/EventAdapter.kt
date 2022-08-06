package com.stubhub.event.ui

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stubhub.event.databinding.ItemRowBinding
import com.stubhub.event.model.Event


/**
 * Event adapter For showing recyclerview Data
 */
class EventAdapter() :
    RecyclerView.Adapter<MainViewHolder>() {

    //Filter list
    private var eventFilterList: MutableList<Event> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val article = eventFilterList[position]

        holder.binding.apply {
            eventId.text = "Id: ".plus(article.id.toString())
            val htmlCity = "City: ".plus("<b>").plus(article.city).plus("</b>")
            city.text = Html.fromHtml(htmlCity)
            eventName.text = "Event name : ".plus(article.name)
            artistList.text = "Artists: ".plus(article.artist.joinToString {
                it
            })
            val htmlPrice = "Price: ".plus("<b>").plus(article.price).plus("</b>")

            price.text = Html.fromHtml(htmlPrice)
        }
    }

    override fun getItemCount(): Int {
        return eventFilterList.size
    }

    //SetData and initialize list
    fun setData(articleModel: MutableList<Event>) {
        eventFilterList = articleModel
        notifyDataSetChanged()
    }

}

class MainViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)
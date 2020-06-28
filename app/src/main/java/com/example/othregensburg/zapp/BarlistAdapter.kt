package com.example.othregensburg.zapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.barlist_item.view.*

class barlistAdapter(val homeFeed: HomeFeed) : RecyclerView.Adapter<CustomViewHolder>(){


    // number of items in the list
    override fun getItemCount(): Int {
        return homeFeed.bars.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.barlist_item, parent, false)

        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        // get one bar object
        val barObj = homeFeed.bars.get(position)

        holder.view.lbl_bar_name?.text = barObj.name
        holder.view.lbl_bar_description?.text = barObj.description

        val barImageView = holder.view.imgView_bar
        Picasso.get().load(barObj.img_preview).into(barImageView)

        holder.bars = barObj
    }

}

class CustomViewHolder(val view: View, var bars: Bars? = null): RecyclerView.ViewHolder(view) {

    companion object{
        var BAR_EXTRA_KEY = "MY_EXTRA_KEY"
        var BAR_TITLE_KEY = "BAR_TITLE"
        var BAR_LOCATION_KEY = "BAR_LOCATION"
        var BAR_SPECIAL_KEY = "BAR_SPECIAL"
        var BAR_IMAGE_KEY = "BAR_IMAGE"
    }

    init {
        view.setOnClickListener {

            //println("Clicked on Viewholder")

            val intent = Intent(view.context, BardetailActivity::class.java)

            intent.putExtra(BAR_EXTRA_KEY,      bars?.id)
            intent.putExtra(BAR_TITLE_KEY,      bars?.name)
            intent.putExtra(BAR_LOCATION_KEY,   bars?.location)
            intent.putExtra(BAR_SPECIAL_KEY,    bars?.special)
            intent.putExtra(BAR_IMAGE_KEY,  bars?.img_bar)

            view.context.startActivity(intent)
        }
    }
}

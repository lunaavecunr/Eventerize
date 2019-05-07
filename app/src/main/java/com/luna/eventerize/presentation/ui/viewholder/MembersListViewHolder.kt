package com.luna.eventerize.presentation.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.parse.ParseUser

class MembersListViewHolder(v: View): RecyclerView.ViewHolder(v)  {

    private val userName: TextView = itemView.findViewById(R.id.adapter_member_list_name)
    private val userImage: ImageView = itemView.findViewById(R.id.adapter_member_list_orga_image)

    fun bindSet(user: ParseUser){

        userName.text = user.fetchIfNeeded().username
    }

}
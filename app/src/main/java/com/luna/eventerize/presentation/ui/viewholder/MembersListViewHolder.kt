package com.luna.eventerize.presentation.ui.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.parse.ParseUser

class MembersListViewHolder(v: View): RecyclerView.ViewHolder(v)  {

    private val userName: TextView = itemView.findViewById(R.id.adapter_member_list_name)
    private val supprBtn: ImageButton = itemView.findViewById(R.id.adapter_member_list_btnsuppr)

    fun bindSet(user: ParseUser, owner: ParseUser, onUserClickListener: ((ParseUser) -> Unit)?){

        userName.text = user.fetchIfNeeded().username
        if (ParseUser.getCurrentUser().objectId == owner.objectId) {
            supprBtn.visibility = View.VISIBLE
        } else {
            supprBtn.visibility = View.INVISIBLE
        }

        supprBtn.setOnClickListener {
            onUserClickListener?.let {
                it(user)
            }
        }
    }

}
package com.luna.eventerize.presentation.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.ui.viewholder.MembersListViewHolder
import com.luna.eventerize.presentation.utils.inflate
import com.parse.ParseUser

class MembersListAdapter: RecyclerView.Adapter<MembersListViewHolder>() {

    private val membersList: MutableList<ParseUser> = mutableListOf()
    private var owner: ParseUser = ParseUser()
    private var onUserClickListener: ((ParseUser)->Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListViewHolder {
        val inflatedView = parent.inflate(R.layout.adapter_member_list, false)
        return MembersListViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = membersList.size

    override fun onBindViewHolder(holder: MembersListViewHolder, position: Int) {
        val userSet = membersList[position]

        holder.bindSet(userSet, this.owner, onUserClickListener)
    }

    fun addUserList(membersList : List<ParseUser>){
        this.membersList.addAll(membersList)
        notifyDataSetChanged()
    }

    fun updateUserList(membersList: List<ParseUser>, owner: ParseUser){
        this.owner = owner
        this.membersList.clear()
        addUserList(membersList)
    }

    fun setOnUserClick(onUserClick: (ParseUser)->Unit){
        onUserClickListener = onUserClick
    }


}
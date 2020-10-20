package com.stephenmorgandevelopment.celero_challeng_kt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.stephenmorgandevelopment.celero_challeng_kt.R
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient

class AllClientsAdapter(
    val context: Context
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)
    private var clientList: List<SimpleClient> = emptyList()

    override fun getCount(): Int {
        return clientList.size
    }

    override fun getItem(position: Int): Any? {
        return clientList[position]
    }

    override fun getItemId(position: Int): Long {
        return clientList[position].identifier
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ClientViewHolder
        var view = View(context)

        if (convertView == null) {
            view = inflater.inflate(R.layout.client_list_item, parent, false)
            holder = ClientViewHolder(
                view.findViewById(R.id.clients_name),
                view.findViewById(R.id.service_reason)
            )
            view.tag = holder
        } else {
            holder = convertView.tag as ClientViewHolder
        }

        val client = clientList[position]
        holder.name.text = client.name
        holder.serviceReason.text = client.serviceReason

        return convertView ?: view
    }

    internal fun setClientList(clients: List<SimpleClient>) {
        clientList = clients
        notifyDataSetChanged()
    }

    inner class ClientViewHolder(
        val name: TextView,
        val serviceReason: TextView
    )
}
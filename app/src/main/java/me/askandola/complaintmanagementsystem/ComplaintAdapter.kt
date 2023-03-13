package me.askandola.complaintmanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import me.askandola.complaintmanagementsystem.models.Complaint

class ComplaintAdapter(options: FirestoreRecyclerOptions<Complaint>, private val clickInterface: ClickInterface) :
    FirestoreRecyclerAdapter<Complaint, ComplaintAdapter.ComplaintViewHolder>(options) {

    class ComplaintViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val status: TextView = view.findViewById(R.id.status)
        val date: TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        val viewHolder = ComplaintViewHolder(view)
        view.setOnClickListener {
            clickInterface.onItemClicked(snapshots.getSnapshot(viewHolder.bindingAdapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int, model: Complaint) {
        holder.title.text = model.title
        holder.date.text = "Date: ${model.date}"
        holder.status.text = "Status: ${model.status}"
    }
}

interface ClickInterface {
    fun onItemClicked(complaintId: String)
}
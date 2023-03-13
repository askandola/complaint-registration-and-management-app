package me.askandola.complaintmanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import me.askandola.complaintmanagementsystem.models.User

class UserAdapter(options: FirestoreRecyclerOptions<User>, private val clickInterface: UserClickInterface) :
    FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder>(options) {

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userNameText)
        val email: TextView = view.findViewById(R.id.userEmailText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_user_list_item, parent, false)
        val viewHolder = UserViewHolder(view)
        view.setOnClickListener {
            clickInterface.onItemClicked(snapshots.getSnapshot(viewHolder.bindingAdapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.userName.text = model.name
        holder.email.text = model.email
    }
}

interface UserClickInterface {
    fun onItemClicked(userId: String)
}
package fr.isen.dufaza.androidtoolbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class RandomUserAdapt (private val items: RandomUser, val context: Context): RecyclerView.Adapter<RandomUserAdapt.ViewHolderRandom>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRandom {
        return ViewHolderRandom(LayoutInflater.from(context).inflate(R.layout.web_contact_list, parent, false))
    }

    override fun getItemCount(): Int {
        return items.results.size
    }

    override fun onBindViewHolder(holder: ViewHolderRandom, position: Int) {


        holder.randomUserName.text = items.results[position].name.first + " "+ items.results[position].name.last
        holder.randomUserAddress.text = items.results[position].location.street.number.toString() + " " +
                items.results[position].location.street.name + ", " + items.results[position].location.city + ", "+
                items.results[position].location.country
        holder.randomUserEmail.text = items.results[position].email
        Picasso.get()
            .load(items.results[position].picture.large)
            .into(holder.randomUserPict)
    }

    class ViewHolderRandom(itemView: View): RecyclerView.ViewHolder(itemView){
        val randomUserName: TextView = itemView.findViewById(R.id.fullName)
        val randomUserAddress: TextView = itemView.findViewById(R.id.adresse)
        val randomUserEmail: TextView = itemView.findViewById(R.id.email)
        val randomUserPict: ImageView = itemView.findViewById(R.id.pictureUser)

    }

}

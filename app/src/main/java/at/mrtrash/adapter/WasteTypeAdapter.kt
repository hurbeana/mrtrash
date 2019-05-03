package at.mrtrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.R
import at.mrtrash.models.WasteType
import kotlinx.android.synthetic.main.waste_type_list_item.view.*

class WasteTypeAdapter(private val wasteTypes: List<WasteType>) :
    RecyclerView.Adapter<WasteTypeAdapter.WasteTypeViewHolder>() {

    class WasteTypeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var wasteType: WasteType? = null

        fun bindWasteType(wasteType: WasteType) {
            this.wasteType = wasteType
            view.list_title.text = wasteType.type
            view.waste_Places.text = wasteType.wastePlaces.joinToString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): WasteTypeAdapter.WasteTypeViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.waste_type_list_item, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters
        return WasteTypeViewHolder(textView)
    }

    override fun onBindViewHolder(holder: WasteTypeViewHolder, position: Int) {
        holder.bindWasteType(wasteType = wasteTypes[position])
    }

    override fun getItemCount() = wasteTypes.size
}

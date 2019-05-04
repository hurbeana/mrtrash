package at.mrtrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.R
import at.mrtrash.fastScrollRecyclerView.FastScrollRecyclerViewInterface
import at.mrtrash.models.WasteType
import kotlinx.android.synthetic.main.waste_type_list_item.view.*
import java.util.HashMap

class WasteTypeAdapter(val wasteTypes: List<WasteType>) :
    RecyclerView.Adapter<WasteTypeAdapter.WasteTypeViewHolder>(),
    FastScrollRecyclerViewInterface {

    var onClick: (pos: Int, type: Int) -> Unit = { _,_ -> Unit}

    override fun getMapIndex(): HashMap<String, Int> {
        val retmap = hashMapOf<String, Int>()
        wasteTypes.forEachIndexed { index, wasteType ->
            val mindex = Character.toString(wasteType.type[0])
            if (!retmap.containsKey(mindex))
                retmap[mindex] = index
        }
        return retmap
    }

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
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.waste_type_list_item, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return WasteTypeViewHolder(cardView).listen(onClick)
    }

    override fun onBindViewHolder(holder: WasteTypeViewHolder, position: Int) {
        holder.bindWasteType(wasteType = wasteTypes[position])
    }

    override fun getItemCount() = wasteTypes.size
}

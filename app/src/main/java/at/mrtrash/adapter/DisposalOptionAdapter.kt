package at.mrtrash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.R
import at.mrtrash.models.Wasteplace
import kotlinx.android.synthetic.main.card_disposal_option.view.*

class DisposalOptionAdapter(private val disposalOptions: ArrayList<Wasteplace>) :
    RecyclerView.Adapter<DisposalOptionAdapter.DisposalOptionViewHolder>() {

    private val TAG = "DisposalOptionAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisposalOptionViewHolder(parent.inflate(R.layout.card_disposal_option))

    override fun getItemCount() = disposalOptions.size

    override fun onBindViewHolder(holder: DisposalOptionViewHolder, position: Int) =
        holder.bindWasteplace(disposalOptions[position])

    class DisposalOptionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var wasteplace: Wasteplace? = null

        fun bindWasteplace(wasteplace: Wasteplace) {
            this.wasteplace = wasteplace
            view.cardTextViewTitle.text = "Mistplatz"
            view.cardTextViewAddress.text = getAddressString(wasteplace)
            view.cardTextViewDistance.text = getDistanceString(wasteplace)
            view.cardTextViewOpeningHours.text = wasteplace.openingHours
        }

        private fun getAddressString(wasteplace: Wasteplace): String {
            return wasteplace.address + ", 1" + wasteplace.district.toString().padStart(2, '0') + "0 Wien"
        }

        private fun getDistanceString(wasteplace: Wasteplace): String {
            return if(wasteplace.distance != null) {
                "${wasteplace.distance.format(2)} km entfernt"
            } else {
                "-"
            }
        }
    }

}
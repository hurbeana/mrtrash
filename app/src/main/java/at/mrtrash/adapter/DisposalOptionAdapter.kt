package at.mrtrash.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.R
import at.mrtrash.models.DisposalOption
import at.mrtrash.models.ProblemMaterialCollectionPoint
import at.mrtrash.models.Wasteplace
import kotlinx.android.synthetic.main.card_disposal_option.view.*

class DisposalOptionAdapter(private val disposalOptions: ArrayList<DisposalOption>) :
    RecyclerView.Adapter<DisposalOptionAdapter.DisposalOptionViewHolder>() {

    private val TAG = "DisposalOptionAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisposalOptionViewHolder(parent.inflate(R.layout.card_disposal_option))

    override fun getItemCount() = disposalOptions.size

    override fun onBindViewHolder(holder: DisposalOptionViewHolder, position: Int) =
        holder.bindDisposalOption(disposalOptions[position])

    class DisposalOptionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var disposalOption: DisposalOption? = null

        fun bindDisposalOption(disposalOption: DisposalOption) {
            this.disposalOption = disposalOption
            view.cardTextViewTitle.text = getTitleString(disposalOption)
            view.cardTextViewAddress.text = getAddressString(disposalOption)
            view.cardTextViewDistance.text = getDistanceString(disposalOption)
            view.cardTextViewOpeningHours.text = disposalOption.openingHours
        }

        private fun getTitleString(disposalOption: DisposalOption): String {
            return when (disposalOption) {
                is Wasteplace -> "Mistplatz"
                is ProblemMaterialCollectionPoint -> "Problemstoffsammelstelle"
                else -> "Entsorgungsmöglichkeit"
            }
        }

        private fun getAddressString(disposalOption: DisposalOption): String {
            return disposalOption.address + ", 1" + disposalOption.district.toString().padStart(2, '0') + "0 Wien"
        }

        private fun getDistanceString(disposalOption: DisposalOption): String {
            return if (disposalOption.distance != null) {
                "${disposalOption.distance?.format(2)} km entfernt"
            } else {
                "-"
            }
        }
    }

}
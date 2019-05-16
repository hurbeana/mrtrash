package at.mrtrash.models.wasteType

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.mrtrash.models.WasteType
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.io.StringReader
import androidx.core.content.res.TypedArrayUtils.getText
import java.util.ArrayList


class WasteTypeListViewModel(private val context: Context) : ViewModel() {

    private val TAG = "WasteTypeListViewModel"

    val original: List<WasteType> by lazy {
        loadWasteTypes()
    }

    val liveWasteTypes: MutableLiveData<List<WasteType>> by lazy {
        MutableLiveData<List<WasteType>>(original)
    }

    fun filter(query: String?) {
        if (query == null) {
            liveWasteTypes.postValue(original)
            return
        }
        val lowerCaseQuery = query.toLowerCase()

        val filteredModelList = ArrayList<WasteType>()
        for (wasteType in original) {
            val text = wasteType.type.toLowerCase()
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(wasteType)
            }
        }
        liveWasteTypes.postValue(filteredModelList)
    }

    private fun loadWasteTypes(): List<WasteType> {

        val wasteTypes = mutableListOf<WasteType>()
        try {
            Log.d(TAG, "LOADING!")
            val inputStream: InputStream = context.assets?.open("muellABC.json")!!
            val klaxon = Klaxon()
            val string = inputStream.bufferedReader().readText()
            JsonReader(StringReader(string)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val wasteType = klaxon.parse<WasteType>(reader)
                        if (wasteType != null) wasteTypes.add(wasteType)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            throw e
        }

        return wasteTypes.also { it.sortBy { wt -> wt.type } }
    }
}

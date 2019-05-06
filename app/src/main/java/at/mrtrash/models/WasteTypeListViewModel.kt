package at.mrtrash.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.io.StringReader

class WasteTypeListViewModel(private val context: Context) : ViewModel() {

    private val TAG = "WasteTypeListViewModel"

    val liveWasteTypes: MutableLiveData<List<WasteType>> by lazy {
        MutableLiveData<List<WasteType>>(loadWasteTypes())
    }

    private fun loadWasteTypes() : List<WasteType> {

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
                        if(wasteType != null) wasteTypes.add(wasteType)
                    }
                }
            }
        } catch (e:Exception){
            Log.d(TAG, e.toString())
            throw e
        }
        //Log.d(TAG, wasteTypes.toString())

        return wasteTypes.also { it.sortBy { wt -> wt.type } }
    }
}

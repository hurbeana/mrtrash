package at.mrtrash.models.wasteType

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mrtrash.models.WasteType
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.io.StringReader
import java.util.ArrayList
import kotlinx.coroutines.*


class WasteTypeListViewModel(private val context: Context) : ViewModel() {

    private val TAG = "WasteTypeListViewModel"

    private val original: MutableList<WasteType> = mutableListOf()

    val liveWasteTypes: MutableLiveData<List<WasteType>> by lazy {
        viewModelScope.launch {
            loadWasteTypes()
        }
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

    private suspend fun loadWasteTypes()  {
        original.clear()

        try {
            Log.d(TAG, "LOADING!")
            withContext(Dispatchers.IO) {
                val inputStream: InputStream = context.assets?.open("muellABC.json")!!
                val klaxon = Klaxon()
                val string = inputStream.bufferedReader().readText()
                JsonReader(StringReader(string)).use { reader ->
                    reader.beginArray {
                        while (reader.hasNext()) {
                            val wasteType = klaxon.parse<WasteType>(reader)
                            if (wasteType != null){
                                original.add(wasteType)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            throw e
        }

        original.sortBy { wt -> wt.type }
        liveWasteTypes.value = original
    }
}

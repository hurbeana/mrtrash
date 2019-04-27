package at.mrtrash.network

import retrofit2.Call
import retrofit2.http.GET

interface DataService {

    @GET("geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:MISTPLATZOGD&srsName=EPSG:4326&outputFormat=json")
    fun getWasteplaces(): Call<WasteplaceResponse>

}
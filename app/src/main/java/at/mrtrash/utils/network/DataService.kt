package at.mrtrash.utils.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * Service to access open data from the web
 */
interface DataService {

    @GET("geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:MISTPLATZOGD&srsName=EPSG:4326&outputFormat=json")
    fun getWasteplaces(): Call<WasteplaceResponse>

    @GET("geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:PROBLEMSTOFFOGD&srsName=EPSG:4326&outputFormat=json")
    fun getProblemMaterialCollectionPoint(): Call<ProblemMaterialCollectionPointResponse>

}
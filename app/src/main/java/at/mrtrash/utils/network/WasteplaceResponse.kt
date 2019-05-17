package at.mrtrash.utils.network

import com.google.gson.annotations.SerializedName

class WasteplaceResponse {
    var type: String? = null
    var totalFeatures: Int? = null
    var features: ArrayList<Feature>? = null

    class Feature {
        var type: String? = null
        var id: String? = null
        var geometry: Geometry? = null
        @SerializedName("geometry_name")
        var geometryName: String? = null
        var properties: Properties? = null

        class Geometry {
            var type: String? = null
            var coordinates: ArrayList<Double>? = null
        }

        class Properties {
            @SerializedName("BEZIRK")
            var district: Int? = null
            @SerializedName("ADRESSE")
            var address: String? = null
            @SerializedName("OBJEKT")
            var objecttype: String? = null
            @SerializedName("OEFFNUNGSZEIT")
            var openingHours: String? = null
        }
    }
}
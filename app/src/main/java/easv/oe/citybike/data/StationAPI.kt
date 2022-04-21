package easv.oe.citybike.data

import retrofit2.Call
import retrofit2.http.GET

interface StationAPI {

    @GET("/gbfs/en/station_information.json")
    fun getAll(): Call<String>
}
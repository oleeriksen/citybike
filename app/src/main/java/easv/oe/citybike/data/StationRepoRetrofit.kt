package easv.oe.citybike.data

import android.util.Log
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import easv.oe.citybike.MainActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class StationRepoRetrofit {

    private val url = "https://gbfs.citibikenyc.com/"


    private val retrofit: Retrofit

    private val stationAPI: StationAPI

    private val stationRequest: Call<String>

    public constructor() {
        retrofit = Retrofit.Builder()
                                         .baseUrl(url)
                                         .addConverterFactory(ScalarsConverterFactory.create())
                                         .build()

        stationAPI = retrofit.create(StationAPI::class.java)

        stationRequest = stationAPI.getAll()


    }

    fun getAll(callback: ICallback){
        stationRequest.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val stations = getStationsFromString( response.body()!! )
                Log.d(MainActivity.TAG, "Stations received - ${stations.size}")
                callback.onStationsReady( stations )
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(MainActivity.TAG, "failure in getAll statusCode = ${t.message}")
            }
        })
    }

    private fun getStationsFromString(jsonString: String?): List<BEStation> {
        val result = ArrayList<BEStation>()

        if (jsonString!!.startsWith("error")) {
            Log.d(MainActivity.TAG, "Error: $jsonString")
            return result
        }
        if (jsonString == null) {
            Log.d(MainActivity.TAG, "Error: NO RESULT")
            return result
        }
        var array: JSONArray?
        try {
            array = JSONObject(jsonString).getJSONObject("data").getJSONArray("stations")
            for (i in 0 until array.length()) {
                result.add(BEStation(array.getJSONObject(i)))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }
}
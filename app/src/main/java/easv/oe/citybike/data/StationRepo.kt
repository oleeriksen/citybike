package easv.oe.citybike.data

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import easv.oe.citybike.MainActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class StationRepo {

    // see https://citibikenyc.com/homepage
    private val url = "https://gbfs.citibikenyc.com/gbfs/en/station_information.json"

    private val httpClient: AsyncHttpClient = AsyncHttpClient()


    fun getAll(callback: ICallback){
        httpClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val stations = getStationsFromString( String(responseBody!!) )
                Log.d(MainActivity.TAG, "Stations received - ${stations.size}")
                callback.onStationsReady( stations )
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(MainActivity.TAG, "failure in getAll statusCode = $statusCode")
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
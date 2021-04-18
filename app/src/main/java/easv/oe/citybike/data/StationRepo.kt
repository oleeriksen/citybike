package easv.oe.citybike.data

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class StationRepo {

    val TAG = "xyz"

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
                callback.onStationsReady( stations )
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "failure in getAll statusCode = $statusCode")
            }

        })
    }

    private fun getStationsFromString(jsonString: String?): List<BEStation> {
        val result = ArrayList<BEStation>()

        if (jsonString!!.startsWith("error")) {
            Log.d(TAG, "Error: $jsonString")
            return result
        }
        if (jsonString == null) {
            Log.d(TAG, "Error: NO RESULT")
            return result
        }
        var all: JSONObject?
        var array: JSONArray?
        try {
            all = JSONObject(jsonString).getJSONObject("data")
            array = all.getJSONArray("stations")
            for (i in 0 until array.length()) {
                result.add(BEStation(array.getJSONObject(i)))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }
}
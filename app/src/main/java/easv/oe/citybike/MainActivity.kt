package easv.oe.citybike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.core.widget.doOnTextChanged
import easv.oe.citybike.data.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "xyz"
    }

    val mRepo = StationRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRepo.getAll(object:ICallback{
            override fun onStationsReady(stations: List<BEStation>) {
                setupListView(stations)
                tvCount.text = stations.size.toString()
            }
        })

        etFilter.doOnTextChanged { text, start, before, count -> mRepo.getAll(object:ICallback{
            override fun onStationsReady(stations: List<BEStation>) {
                val filter = text.toString()
                val filteredStations = stations.filter {station -> station.name.contains(filter)}
                setupListView(filteredStations)
                tvCount.text = filteredStations.size.toString()
            }
        }) }

        Log.d(TAG, "onCreate done")
    }

    fun setupListView(stations: List<BEStation>) {

        val adapter: ListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            stations
        )
        lvStations.adapter = adapter

        Log.d(TAG, "Listview adapter created with ${stations.size} stations")
    }
}
package easv.oe.citybike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import easv.oe.citybike.data.*
import kotlinx.android.synthetic.main.activity_filter.*

class MainActivity : AppCompatActivity() {

    val mRepo = StationRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        mRepo.getAll(object:ICallback{
            override fun onStationsReady(stations: List<BEStation>) {
                setupListView(stations)
                tvCount.text = stations.size.toString()
            }
        })

        etFilter.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mRepo.getAll(object:ICallback{
                    override fun onStationsReady(stations: List<BEStation>) {
                        val filter = p0.toString()
                        val filteredStations = stations.filter {station -> station.name.contains(filter)}
                        setupListView(filteredStations)
                        tvCount.text = filteredStations.size.toString()
                    }
                })

            }

            override fun afterTextChanged(p0: Editable?) {}
        })


    }

    fun setupListView(stations: List<BEStation>) {
        val asStrings = stations.map { s -> "${s.id}, ${s.name}"}
        val adapter: ListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            asStrings.toTypedArray()
        )
        lvStations.adapter = adapter
    }
}
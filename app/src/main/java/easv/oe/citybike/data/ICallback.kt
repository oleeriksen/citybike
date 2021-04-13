package easv.oe.citybike.data

interface ICallback {

    fun onStationsReady(stations: List<BEStation>)

}
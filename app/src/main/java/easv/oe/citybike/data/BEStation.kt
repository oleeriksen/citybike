package easv.oe.citybike.data

import org.json.JSONObject

class BEStation(val id: Int, val name: String) {

    constructor(jsonObject: JSONObject) : this(jsonObject["id"] as Int, jsonObject["stationName"] as String)
}
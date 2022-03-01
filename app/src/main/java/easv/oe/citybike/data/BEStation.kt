package easv.oe.citybike.data

import org.json.JSONObject

class BEStation(val id: Int, val name: String) {

    constructor(jsonObject: JSONObject) :
            this(Integer.parseInt(jsonObject["station_id"] as String),
                 jsonObject["name"] as String)
}
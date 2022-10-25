package com.example.practical5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.motion.widget.Debug
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class FindHoliday : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_holiday)
    }

    /**
     * This method changes the textview to show the cat fact passed as a parameter.
     * @param factText This variable contains the String of the fact, to be shown in the textview
     */
    fun setHoliday(holidayText: String) {
        val textV = findViewById<TextView>(R.id.holidayTextView)
        textV.text = holidayText

    }

    /**
     * This is the method attached to the button onClick property
     */
    fun getHoliday(view: View){
        setHoliday("Loading...")
        /////// PLEASE CHANGE THIS TO YOUR OWN API KEY!!!! ///////
        var url = "https://calendarific.com/api/v2/holidays?&api_key=ce0f9b4ce5464c0373856ce4a068674680ea7bdc&country=in-wb&year=2050"
        /////// CHANGE THE API KEY ^^^^^.
        try{
            getDataFromServer(url) //Here we call the function that will connect to the server
        }
        catch (e: IOException){ //The getDataFromServer function can "throw" an IOException if there is an error. We have to "catch" that here, otherwise our entire app will crash.
            setHoliday("Error connecting to server")
            return
        }
    }

    /**
     * Method that uses OkHTTP to connect to the server
     * @param url the URL of the remote data source
     * @throws IOException
     */
    private fun getDataFromServer(url:String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback { //This is an inner class that will be used to handle the response.

            override fun onFailure(call: Call, e: IOException) { //If there is an error in the response...
                e.printStackTrace() //Print the error to the console
            }

            override fun onResponse(call: Call, response: Response) { //If the response is good...
                response.use{
                    if (!response.isSuccessful) throw IOException ("Unexpected code $response") // Ensure that we throw an exception if response is not successful
                    readJSONFact(response.body!!.string()) //send the JSON we got from the server to the readJSONFact function.
                }
            }
        })
    }

    /**
     * Method that takes a string containing JSON, and extracts the Cat Fact.
     * @param rawJson A string containing JSON encoded daa
     */
    fun readJSONFact(rawJson:String){
        runOnUiThread(java.lang.Runnable { //This section has to happen on the same thread as the user interface.
            try {
                var json = JSONObject(rawJson) //Convert the string into a JSONArray
                var holiday = json.getJSONObject("response").getJSONArray("holidays").getJSONObject(3).getString("name")
                setHoliday(holiday)
            }
            catch (e: JSONException){ //Handle any issues where the JSON is badly formed or invalid
                setHoliday("Invalid JSON text")
                e.printStackTrace()

            }
        })
    }
}
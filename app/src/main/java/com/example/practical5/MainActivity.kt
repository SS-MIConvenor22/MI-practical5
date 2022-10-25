package com.example.practical5

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /**
     * This method changes the textview to show the cat fact passed as a parameter.
     * @param factText This variable contains the String of the fact, to be shown in the textview
     */
    fun setFact(factText: String,categoryText:String) {
        val textV = findViewById<TextView>(R.id.factTextView)
        textV.text = factText
        val textV2 = findViewById<TextView>(R.id.excuseCategory)
        textV2.text = categoryText
    }

    /**
     * This is the method attached to the button onClick property
     */
    fun getFact(view: View){
        setFact("Loading your excuse...","")
        var url = "https://excuser.herokuapp.com/v1/excuse"

        try{
            getDataFromServer(url) //Here we call the function that will connect to the server
        }
        catch (e:IOException){ //The getDataFromServer function can "throw" an IOException if there is an error. We have to "catch" that here, otherwise our entire app will crash.
            setFact("Error connecting to server","")
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
        client.newCall(request).enqueue(object : Callback{ //This is an inner class that will be used to handle the response.

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
                var json = JSONArray(rawJson) //Convert the string into a JSONArray
                var excuse = json.getJSONObject(0).getString("excuse") //Extract the excuse from the first element in the array
                var excuseCategory = json.getJSONObject(0).getString("category")
                setFact(excuse,excuseCategory) //call the method that will set the text on screen to show the excuse
            }
            catch (e: JSONException){ //Handle any issues where the JSON is badly formed or invalid
                setFact("Invalid JSON text","")

            }
        })
    }
}
package com.example.sunriseapp
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "Nevsehir"//City name
    val API: String = "3de1590440fdbbbb6e6b22358c85c6e0" // Use API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()

    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            // Showing the ProgressBar, Making the main design GONE
            loader.visibility = View.VISIBLE
            mainContainer.visibility = View.GONE
           errorText.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                // Extracting JSON returns from the API
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val clouds = jsonObj.getJSONObject("clouds")
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val cloud= "%" + clouds.getString("all")
                val humidity = "%" + main.getString("humidity")

                val sunrise : Long = sys.getLong("sunrise")
                val sunset : Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed") + " km/s"
                val weatherDescription = weather.getString("description")

                val adress = jsonObj.getString("name")+", "+sys.getString("country")

                // Populating extracted data into our views

                adres.text = adress
                durum.text = weatherDescription.capitalize()
                sicaklik.text = temp
                min_sicaklik.text = tempMin
                max_sicaklik.text = tempMax
                gunDogumu.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                gunBatimi.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                ruzgar.text = windSpeed
                bulut.text = cloud
                nem.text = humidity

                // Views populated, Hiding the loader, Showing the main design
                loader.visibility = View.GONE
                mainContainer.visibility = View.VISIBLE

            } catch (e: Exception) {
                loader.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }

        }
    }
}
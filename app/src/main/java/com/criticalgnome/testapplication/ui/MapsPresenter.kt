package com.criticalgnome.testapplication.ui

import android.content.Context
import android.location.Geocoder
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.criticalgnome.testapplication.Constants
import com.criticalgnome.testapplication.entity.Cities
import com.criticalgnome.testapplication.entity.City
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.util.*

@InjectViewState
class MapsPresenter : MvpPresenter<MapsView>() {

    private var score: Double = Constants.START_SCORE
    private var highScore: Int = 0
    private var citiesCount: Int = 0
    private val cities: MutableList<City> = arrayListOf()
    private lateinit var currentCity: City

    fun onHighScoreLoaded(savedScore: Int) {
        highScore = savedScore
    }

    fun onNewRandomCityRequested(context: Context, inputStream: InputStream) {
        val jsonString = try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            ""
        }
        val citiesFromJson: Cities = Gson().fromJson(jsonString, Cities::class.java)
        for(city in citiesFromJson.capitalCities) {
            val name = city.capitalCity
            val gc = Geocoder(context)
            val addresses = gc.getFromLocationName(name, 1)
            cities.add(City(name, addresses[0].latitude, addresses[0].longitude))
        }
        currentCity = cities[(0..cities.size).random()]
        viewState.setCityName(currentCity.name)
        viewState.setScore(score.toInt())
        viewState.showAttemptsCount(citiesCount, highScore)
    }

    fun onMarkerPlaced(currentPosition: LatLng) {
        val distance = onDistanceCalculatingRequested(currentCity.latitude, currentPosition.latitude, currentCity.longitude, currentPosition.longitude)
        if (distance < 50) {
            citiesCount++
            if (citiesCount > highScore) highScore = citiesCount
            viewState.setNewHighScore(highScore)
            viewState.winGame()
        } else {
            score -= distance
            viewState.setScore(score.toInt())
            if (score < 0) {
                score = Constants.START_SCORE
                citiesCount = 0
                viewState.loseGame()
            } else {
                viewState.showAttemptsCount(citiesCount, highScore)
                viewState.showMistakenDialog(distance.toInt(), currentCity)
            }
        }

    }

    private fun onDistanceCalculatingRequested(lat1: Double, lat2: Double, lon1: Double, lon2: Double) :Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515
        dist *= 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180 / Math.PI
    }

    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start

}

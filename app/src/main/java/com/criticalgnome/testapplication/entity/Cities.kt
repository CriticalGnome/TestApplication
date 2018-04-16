package com.criticalgnome.testapplication.entity

import com.google.gson.annotations.SerializedName

data class Cities(@SerializedName("capitalCities") val capitalCities: List<City>) {

    data class City(@SerializedName("capitalCity") val capitalCity: String)

}
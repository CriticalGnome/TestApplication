package com.criticalgnome.testapplication.ui

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.criticalgnome.testapplication.Constants.PAUSE_DURATION
import com.criticalgnome.testapplication.Constants.PAUSE_STEP
import com.criticalgnome.testapplication.Constants.SHARED_PREFS_KEY
import com.criticalgnome.testapplication.Constants.SHARED_PREFS_NAME
import com.criticalgnome.testapplication.Constants.START_LATITUDE
import com.criticalgnome.testapplication.Constants.START_LONGITUDE
import com.criticalgnome.testapplication.Constants.START_ZOOM
import com.criticalgnome.testapplication.R
import com.criticalgnome.testapplication.entity.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : MvpAppCompatActivity(), MapsView, OnMapReadyCallback {

    @BindView(R.id.citiesText)      lateinit var citiesText: TextView
    @BindView(R.id.scoreText)       lateinit var scoreText: TextView
    @BindView(R.id.cityText)        lateinit var cityText: TextView
    @BindView(R.id.placeButton)     lateinit var placeButton: Button
    @BindView(R.id.centerMarker)    lateinit var centerMarker: ImageView
    @BindView(R.id.countdown)       lateinit var countdown: TextView

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var presenter: MapsPresenter

    private lateinit var mMap: GoogleMap
    private lateinit var prefs: SharedPreferences

    @ProvidePresenterTag(presenterClass = MapsPresenter::class, type = PresenterType.GLOBAL)
    fun provideDialogPresenterTag(): String = "Main"

    @ProvidePresenter(type = PresenterType.GLOBAL)
    fun provideDialogPresenter() = MapsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        ButterKnife.bind(this)
        prefs = this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val highScore: Int = prefs.getInt(SHARED_PREFS_KEY, 0)
        presenter.onHighScoreLoaded(highScore)
        presenter.onNewRandomCityRequested(this, resources.openRawResource(R.raw.capital_cities))
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(START_LATITUDE, START_LONGITUDE), START_ZOOM))
        placeButton.isEnabled = true
    }

    override fun showAttemptsCount(count: Int, highScore: Int) {
        citiesText.text = getString(R.string.cities_placed, count, highScore)
    }

    override fun setScore(score: Int) {
        scoreText.text = getString(R.string.kilometers_left, score)
    }

    override fun setCityName(cityName: String) {
        cityText.text = getString(R.string.double_quotes, cityName)
    }

    override fun setNewHighScore(highScore: Int) {
        prefs.edit().putInt(SHARED_PREFS_KEY, highScore).apply()
    }

    override fun winGame() = AlertDialog.Builder(this).setTitle(getString(R.string.congratulation))
            .setMessage(getString(R.string.you_right))
            .setPositiveButton(getString(R.string.next_round), { _: DialogInterface, _: Int -> recreate() })
            .setCancelable(false)
            .create()
            .show()

    override fun loseGame() = AlertDialog.Builder(this).setTitle(getString(R.string.sorry))
            .setMessage(getString(R.string.you_lose))
            .setPositiveButton(getString(R.string.next_round), { _: DialogInterface, _: Int -> recreate() })
            .setCancelable(false)
            .create()
            .show()

    override fun showMistakenDialog(distance: Int, currentCity: City) {
        mMap.addMarker(MarkerOptions()
                .position(LatLng(currentCity.latitude, currentCity.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        countdown.visibility = View.VISIBLE
        centerMarker.visibility = View.GONE
        placeButton.isEnabled = false
        object : CountDownTimer(PAUSE_DURATION, PAUSE_STEP) {
            override fun onTick(millisUntilFinished: Long) {
                countdown.text = (millisUntilFinished / PAUSE_STEP).toString()
            }
            override fun onFinish() {
                recreate()
            }
        }.start()
    }

    @OnClick(R.id.placeButton)
    fun onPlaceButtonClicked() {
        mMap.clear()
        val currentPosition = LatLng(mMap.cameraPosition.target.latitude, mMap.cameraPosition.target.longitude)
        mMap.addMarker(MarkerOptions().position(currentPosition))
        presenter.onMarkerPlaced(currentPosition)
    }

}

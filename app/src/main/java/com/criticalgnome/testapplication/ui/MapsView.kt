package com.criticalgnome.testapplication.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.criticalgnome.testapplication.entity.City

@StateStrategyType(AddToEndSingleStrategy::class)
interface MapsView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun winGame()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun loseGame()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMistakenDialog(distance: Int, currentCity: City)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAttemptsCount(count: Int, highScore: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCityName(cityName: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setScore(score: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNewHighScore(highScore: Int)

}

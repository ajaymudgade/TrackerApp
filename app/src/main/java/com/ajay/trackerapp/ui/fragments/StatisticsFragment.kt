package com.ajay.trackerapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ajay.trackerapp.R
import com.ajay.trackerapp.other.CustomMarkerView
import com.ajay.trackerapp.other.TrackingUtility
import com.ajay.trackerapp.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    private lateinit var tvTotalDistanceInfo: MaterialTextView
    private lateinit var tvTotalDistance: MaterialTextView
    private lateinit var tvTotalTimeInfo: MaterialTextView
    private lateinit var tvTotalTime: MaterialTextView
    private lateinit var tvTotalCaloriesInfo: MaterialTextView
    private lateinit var tvTotalCalories: MaterialTextView
    private lateinit var tvAverageSpeedInfo: MaterialTextView
    private lateinit var tvAverageSpeed: MaterialTextView
    private lateinit var guideline: Guideline
    private lateinit var guideline2: Guideline
    private lateinit var barChart: BarChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTotalDistanceInfo = view.findViewById(R.id.tvTotalDistanceInfo)
        tvTotalDistance = view.findViewById(R.id.tvTotalDistance)
        tvTotalTimeInfo = view.findViewById(R.id.tvTotalTimeInfo)
        tvTotalTime = view.findViewById(R.id.tvTotalTime)
        tvTotalCaloriesInfo = view.findViewById(R.id.tvTotalCaloriesInfo)
        tvTotalCalories = view.findViewById(R.id.tvTotalCalories)
        tvAverageSpeedInfo = view.findViewById(R.id.tvAverageSpeedInfo)
        tvAverageSpeed = view.findViewById(R.id.tvAverageSpeed)
        guideline = view.findViewById(R.id.guideline)
        guideline2 = view.findViewById(R.id.guideline2)
        barChart = view.findViewById(R.id.barChart)

        subscribeToObservers()
        setupBarChart()

    }

    private fun setupBarChart() {
        barChart.xAxis.apply {
            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            axisLineColor = R.color.white
            textColor = R.color.white
            setDrawLabels(false)
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = R.color.white
            textColor = R.color.white
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = R.color.white
            textColor = R.color.white
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }

    }

    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner) {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTimeRun
            }
        }

        viewModel.totalDistance.observe(viewLifecycleOwner) {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}km"
                tvTotalDistance.text = totalDistanceString
            }
        }

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner) {
            it?.let {
                val totalCalories = "${it}kcal"
                tvTotalCalories.text = totalCalories
            }
        }

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner) {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                tvAverageSpeed.text = avgSpeedString
            }
        }

        viewModel.runSortedByDate.observe(viewLifecycleOwner) {
            it?.let {
                val allAvgSpeed = it.indices.map { i ->
                    BarEntry(i.toFloat(), it[i].avgSpeedInKMH)
                }
                val barDataSet = com.github.mikephil.charting.data.BarDataSet(
                    allAvgSpeed,
                    "Avg Speed Over Time").apply {
                        valueTextColor = R.color.white
                        color = R.color.colorAccent
                }
                barChart.data = com.github.mikephil.charting.data.BarData(barDataSet)
                barChart.marker = CustomMarkerView(
                    it.reversed(),
                    requireContext(),
                    R.layout.marker_view)
                barChart.invalidate()
            }
        }

    }

}
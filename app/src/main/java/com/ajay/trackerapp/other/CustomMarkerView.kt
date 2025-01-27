package com.ajay.trackerapp.other

import android.content.Context
import android.widget.TextView
import com.ajay.trackerapp.R
import com.ajay.trackerapp.db.Run
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomMarkerView(val runs: List<Run>, c: Context, layoutId: Int)
    : com.github.mikephil.charting.components.MarkerView(c, layoutId) {

    private val tvDate: TextView = findViewById(R.id.tvDate)
    private val tvAvgSpeed: TextView = findViewById(R.id.tvAvgSpeed)
    private val tvDistance: TextView = findViewById(R.id.tvDistance)
    private val tvDuration: TextView = findViewById(R.id.tvDuration)
    private val tvCaloriesBurned: TextView = findViewById(R.id.tvCaloriesBurned)

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e==null){
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        tvDate.text = dateFormat.format(calendar.time)

        tvAvgSpeed.text = "${run.avgSpeedInKMH} km/h"
        tvDistance.text = "${run.distanceInMeters / 1000f} km"
        tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
        tvCaloriesBurned.text = "${run.caloriesBurned} kcal"
    }
}
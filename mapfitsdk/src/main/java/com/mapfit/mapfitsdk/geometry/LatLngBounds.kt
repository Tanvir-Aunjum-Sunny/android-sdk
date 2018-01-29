package com.mapfit.mapfitsdk.geometry


/**
 * Created by dogangulcan on 1/4/18.
 */
class LatLngBounds(
    var northEast: LatLng = LatLng(),
    var southWest: LatLng = LatLng()
) {

    val center by lazy {
        getCenterLatlng(listOf(northEast, southWest))
    }

    private constructor(builder: Builder) : this() {

        var south: Double? = null
        var west: Double? = null
        var north: Double? = null
        var east: Double? = null

        builder.latLngList.forEach {
            if (south == null || south!! > it.lat) south = it.lat
            if (west == null || west!! > it.lon) west = it.lon
            if (north == null || north!! < it.lat) north = it.lat
            if (east == null || east!! < it.lon) east = it.lon
        }

        northEast = LatLng(north ?: 0.0, east ?: 0.0)
        southWest = LatLng(south ?: 0.0, west ?: 0.0)
    }

    class Builder {

        internal val latLngList = mutableListOf<LatLng>()

        fun include(latLng: LatLng) {
            latLngList.add(latLng)
        }

        fun build() = LatLngBounds(this)
    }

    fun getVisibleBounds(viewWidth: Int, viewHeight: Int, padding: Float): Pair<LatLng, Float> {

        val ry1 = Math.log( (Math.sin(Math.toRadians(southWest.lat)) + 1) / Math.cos(Math.toRadians(southWest.lat)))
        val ry2 = Math.log(
            Math.sin(Math.toRadians(northEast.lat)) + 1
        ) / Math.cos(Math.toRadians(northEast.lat))
        val ryc = (ry1 + ry2) / 2
        val centerX = Math.toDegrees(Math.atan(Math.sinh(ryc)))

        val resolutionHorizontal = (northEast.lon - southWest.lon) / (viewWidth * padding)

        val vy0 = Math.log(Math.tan(Math.PI * (0.25 + centerX / 360)))
        val vy1 = Math.log(Math.tan(Math.PI * (0.25 + northEast.lat / 360)))
        val viewHeightHalf = (viewHeight * padding) / 2.0f
        val zoomFactorPowered = viewHeightHalf / (40.7436654315252 * (vy1 - vy0))
        val resolutionVertical = 360.0 / (zoomFactorPowered * 256)

        val resolution = Math.max(resolutionHorizontal, resolutionVertical)
        val zoom = kotlin.math.log(360 / (resolution * 512), 2.0)

        return Pair(center, zoom.toFloat())
    }


}
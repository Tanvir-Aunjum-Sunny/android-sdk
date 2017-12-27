package com.mapfit.mapfitsdk.annotations.base

/**
 * Created by dogangulcan on 12/19/17.
 */
enum class AnnotationStyle(val style: String) {
    POINT("{ style: 'points', color: 'white', size: [50px, 50px], order: 20000, collide: false }"),
    POLYLINE("{ style: 'lines', color: '#06a6d4', width: 5px, order: 2000 }"),
    POLYGON("{ style: 'polygons', color: '#06a6d4', width: 5px, order: 2000 }")
}

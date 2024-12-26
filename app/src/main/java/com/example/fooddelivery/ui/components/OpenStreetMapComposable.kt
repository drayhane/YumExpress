package com.example.fooddelivery.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import android.os.AsyncTask
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fooddelivery.R
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader


@Composable
fun OpenStreetMapComposable(
    context: Context,
    startPoint: GeoPoint,
    endPoint: GeoPoint,
    modifier: Modifier = Modifier
) {
    // Charger la configuration d'OSMDroid
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    // Créer une vue MapView en utilisant AndroidView
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)

                val startMarker = Marker(this).apply {
                    position = startPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Point de départ"
                    setIcon(resources.getDrawable(R.drawable.marker, null))
                }
                val endMarker = Marker(this).apply {
                    position = endPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Point de fin"
                    setIcon(resources.getDrawable(R.drawable.marker, null))
                }
                overlays.add(startMarker)
                overlays.add(endMarker)

                FetchRouteTask(this).execute(startPoint, endPoint)

                // Calculer une BoundingBox pour inclure les deux points
                val boundingBox = org.osmdroid.util.BoundingBox(
                    maxOf(startPoint.latitude, endPoint.latitude),  // Latitude maximale
                    maxOf(startPoint.longitude, endPoint.longitude), // Longitude maximale
                    minOf(startPoint.latitude, endPoint.latitude),  // Latitude minimale
                    minOf(startPoint.longitude, endPoint.longitude) // Longitude minimale
                )

                // Ajuster le centre et le zoom en fonction de la BoundingBox
                post {
                    val zoomLevel = 15.0

                    // Centrer la carte à la bounding box mais avec ajustement pour ne pas afficher les 15% du haut et 40% du bas
                    val adjustedBoundingBox = getAdjustedBoundingBox(boundingBox, width, height)

                    controller.setCenter(adjustedBoundingBox.center)
                    controller.setZoom(zoomLevel)
                }
            }
        }
    )
}

// Fonction pour ajuster la BoundingBox pour être dans la portion visible du milieu
fun getAdjustedBoundingBox(boundingBox: org.osmdroid.util.BoundingBox, screenWidth: Int, screenHeight: Int): org.osmdroid.util.BoundingBox {
    // Get the geographical range of the bounding box
    val latitudeDifference = boundingBox.latNorth - boundingBox.latSouth
    val longitudeDifference = boundingBox.lonEast - boundingBox.lonWest

    // Calculate the 15% and 40% areas based on screen height
    val topPercentage = 0.15f
    val bottomPercentage = 0.40f

    // Adjust the north and south latitude based on the screen proportions
    val adjustedLatNorth = boundingBox.latNorth - (latitudeDifference * topPercentage)
    val adjustedLatSouth = boundingBox.latSouth + (latitudeDifference * bottomPercentage)

    // You can also adjust the east-west boundaries, but for vertical focus, leave them as is.
    return org.osmdroid.util.BoundingBox(
        adjustedLatNorth,
        boundingBox.lonEast,
        adjustedLatSouth,
        boundingBox.lonWest
    )
}

// Tâche pour récupérer l'itinéraire depuis l'API OSRM
class FetchRouteTask(val mapView: MapView) : AsyncTask<GeoPoint, Void, List<GeoPoint>>() {
    override fun doInBackground(vararg params: GeoPoint?): List<GeoPoint> {
        val startPoint = params[0]
        val endPoint = params[1]

        // URL de l'API OSRM (ou autre API)
        val urlString = "https://router.project-osrm.org/route/v1/driving/" +
                "${startPoint?.longitude},${startPoint?.latitude};${endPoint?.longitude},${endPoint?.latitude}" +
                "?overview=full&geometries=geojson"

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }

        reader.close()

        // Analyser la réponse GeoJSON
        val jsonResponse = JSONObject(response.toString())
        val routes = jsonResponse.getJSONArray("routes")
        val geometry = routes.getJSONObject(0).getJSONObject("geometry")
        val coordinates = geometry.getJSONArray("coordinates")

        // Convertir les coordonnées en liste de GeoPoint
        val geoPoints = mutableListOf<GeoPoint>()
        for (i in 0 until coordinates.length()) {
            val coordinate = coordinates.getJSONArray(i)
            val latitude = coordinate.getDouble(1)
            val longitude = coordinate.getDouble(0)
            geoPoints.add(GeoPoint(latitude, longitude))
        }

        return geoPoints
    }

    override fun onPostExecute(result: List<GeoPoint>?) {
        super.onPostExecute(result)

        // Dessiner l'itinéraire sur la carte
        if (result != null) {
            val route = Polyline().apply {
                setPoints(result)
                outlinePaint.color = android.graphics.Color.rgb(255, 100, 13)
            }
            mapView.overlays.add(route)

            // Ajuster la vue en fonction de l'itinéraire
            val boundingBox = org.osmdroid.util.BoundingBox(
                result.maxOf { it.latitude },
                result.maxOf { it.longitude },
                result.minOf { it.latitude },
                result.minOf { it.longitude }
            )
            mapView.zoomToBoundingBox(boundingBox, true)
        }
    }

}


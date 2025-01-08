package com.example.fooddelivery.ui.components

import android.content.Context
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fooddelivery.R
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun OpenStreetMapComposable(
    context: Context,
    startPoint: GeoPoint,
    endPoint: GeoPoint,
    modifier: Modifier = Modifier
) {
    // Charger la configuration d'OSMDroid
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {

                setTileSource(TileSourceFactory.MAPNIK)
                // Ajouter les marqueurs de début et de fin
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

                // Récupérer et afficher l'itinéraire
                FetchRouteTask(this).execute(startPoint, endPoint)

                // Calculer et ajuster la bounding box
                val boundingBox = BoundingBox(
                    maxOf(startPoint.latitude, endPoint.latitude),
                    maxOf(startPoint.longitude, endPoint.longitude),
                    minOf(startPoint.latitude, endPoint.latitude),
                    minOf(startPoint.longitude, endPoint.longitude)
                )

                post {

                    zoomToBoundingBox(boundingBox, false)


                    controller.setZoom(13.0)  //
                    controller.setCenter(startPoint)
                }
            }
        }
    )
}

class FetchRouteTask(private val mapView: MapView) : android.os.AsyncTask<GeoPoint, Void, List<GeoPoint>>() {
    override fun doInBackground(vararg params: GeoPoint?): List<GeoPoint> {
        val startPoint = params[0]
        val endPoint = params[1]
        val urlString = "https://router.project-osrm.org/route/v1/driving/" +
                "${startPoint?.longitude},${startPoint?.latitude};${endPoint?.longitude},${endPoint?.latitude}" +
                "?overview=full&geometries=geojson"

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        reader.useLines { lines -> lines.forEach { response.append(it) } }

        val jsonResponse = JSONObject(response.toString())
        val routes = jsonResponse.getJSONArray("routes")
        val geometry = routes.getJSONObject(0).getJSONObject("geometry")
        val coordinates = geometry.getJSONArray("coordinates")

        return (0 until coordinates.length()).map {
            val coordinate = coordinates.getJSONArray(it)
            GeoPoint(coordinate.getDouble(1), coordinate.getDouble(0))
        }
    }

    override fun onPostExecute(result: List<GeoPoint>?) {
        super.onPostExecute(result)
        if (result != null) {
            val route = Polyline().apply {
                setPoints(result)
                outlinePaint.color = Color.rgb(255, 100, 13)
            }
            mapView.overlays.add(route)

            val boundingBox = BoundingBox(
                result.maxOf { it.latitude },
                result.maxOf { it.longitude },
                result.minOf { it.latitude },
                result.minOf { it.longitude }
            )

            mapView.post {
                // Appliquer la bounding box sans réajuster automatiquement le zoom
                mapView.zoomToBoundingBox(boundingBox, false)

                // Appliquer un zoom bas après la bounding box
                mapView.controller.setZoom(13.0)  // Forcer un zoom plus bas
            }
        }
    }
}

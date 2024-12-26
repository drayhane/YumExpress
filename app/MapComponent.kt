import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class MapComponent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser la configuration OSMDroid
        Configuration.getInstance().load(applicationContext, applicationContext.getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContent {
            MapScreen(
                startPoint = GeoPoint(48.8566, 2.3522),  // Exemple : Paris
                endPoint = GeoPoint(51.5074, -0.1278)   // Exemple : Londres
            )
        }
    }
}

@Composable
fun MapScreen(startPoint: GeoPoint, endPoint: GeoPoint) {
    AndroidView(factory = { context ->
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            // Centrer la carte sur le point de départ
            controller.setZoom(5.0)
            controller.setCenter(startPoint)

            // Ajouter un marqueur pour le point de départ
            val startMarker = Marker(this).apply {
                position = startPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Point de départ"
            }
            overlays.add(startMarker)

            // Ajouter un marqueur pour le point d'arrivée
            val endMarker = Marker(this).apply {
                position = endPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Point d'arrivée"
            }
            overlays.add(endMarker)

            // Tracer un itinéraire entre les deux points
            val polyline = Polyline().apply {
                setPoints(listOf(startPoint, endPoint))
            }
            overlays.add(polyline)
        }
    })
}

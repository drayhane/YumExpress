import com.example.fooddelivery.data.model.Restaurant

class SyncDatabasesUseCaseR(private val repository: restoRepository) {
    suspend operator fun invoke(remoteR: List<Restaurant>) {
        repository.syncResto(remoteR)
    }
}
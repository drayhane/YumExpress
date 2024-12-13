class SyncDatabasesUseCase(private val repository: CountryRepository) {
    suspend operator fun invoke(remoteCountries: List<Country>) {
        repository.syncCountries(remoteCountries)
    }
}

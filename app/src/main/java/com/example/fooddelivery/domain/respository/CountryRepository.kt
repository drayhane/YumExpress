interface CountryRepository {
    suspend fun getCountries(): List<Country>
    suspend fun syncCountries(remoteCountries: List<Country>)
}

interface CountryRepository {
    suspend fun getCountries(): List<Country>
    suspend fun syncCountries(remoteCountries: List<Country>)
}

class CountryRepositoryImpl : CountryRepository {

    override suspend fun getCountries(): List<Country> {
        return fetchCountriesFromSupabase()
    }

    override suspend fun syncCountries(remoteCountries: List<Country>) {
    }
}

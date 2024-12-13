class GetCountriesUseCase(private val repository: CountryRepository) {
    suspend operator fun invoke(): List<Country> {
        return repository.getCountries()
    }
}

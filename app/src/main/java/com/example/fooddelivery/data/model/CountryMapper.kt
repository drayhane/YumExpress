fun CountryEntity.toDomainModel(): Country {
    return Country(id = this.id, name = this.name)
}

fun Country.toEntity(): CountryEntity {
    return CountryEntity(id = this.id, name = this.name)
}

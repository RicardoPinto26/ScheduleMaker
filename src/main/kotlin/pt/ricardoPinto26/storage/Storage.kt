package pt.ricardoPinto26.storage

interface Storage<K, T> {
    val serializer: StringSerializer<T>
    fun load(id: K): T
    fun save(id: K, obj: T)
    fun delete(id: K)
}
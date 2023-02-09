package pt.ricardoPinto26.storage

import java.io.File

class FileStorage<T>(
    override val serializer: StringSerializer<T>
) : Storage<String, T> {
    override fun load(id: String): T = serializer.parse(File(id).readText())

    override fun save(id: String, obj: T) {
        File(id).writeText(serializer.write(obj))
    }

    override fun delete(id: String) {
        File(id).delete()
    }
}
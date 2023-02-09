package pt.ricardoPinto26.model

enum class Day {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY;

    companion object {
        operator fun invoke(index: Int): Day {
            require(index in 0..values().size)
            return values()[index]
        }

        fun parse(name: String): Day =
            Day.values().firstOrNull { it.name == name } ?: throw IllegalArgumentException("No such day exists")
    }
}

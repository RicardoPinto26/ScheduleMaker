package pt.ricardoPinto26.model

data class Schedule(val label: String, val subjects: List<Subject>) {
    init {
        require(label.isNotEmpty()) { "Label ($label) cannot be empty" }
    }

    companion object {
        val EMPTY_SCHEDULE = Schedule("Empty", emptyList())
    }
}
private const val MAX_CLASSES = 5

fun computeSchedules(subjects: List<Subject>): List<Schedule> {
    var schedules = listOf<Schedule>()
    val semester = 4
    val range = (semester * 10)..(semester * 10 + MAX_CLASSES)
    for (ls in range) for (pc in range) for (lae in range) for (sisInf in range) {
        val newSchedule = Schedule(
            label = "$ls,$pc,$lae,$sisInf",
            subjects = listOf(
                subjects.firstOrNull { it.name.contains("LAE") && it.classId == lae } ?: continue,
                subjects.firstOrNull { it.name.contains("LS") && it.classId == ls } ?: continue,
                subjects.firstOrNull { it.name.contains("PC") && it.classId == pc } ?: continue,
                subjects.firstOrNull { it.name.contains("SisInf") && it.classId == sisInf } ?: continue
            )
        )
        if (
            newSchedule.subjects.all { sub1 ->
                newSchedule.subjects.all { sub2 ->
                    sub1 == sub2 || sub1.isCompatible(sub2)
                }
            }
        )
            schedules = schedules + newSchedule
    }
    return schedules
}
package pt.ricardoPinto26.model

data class Schedule(val label: String, val subjects: List<Subject>) {
    init {
        require(label.isNotEmpty()) { "Label ($label) cannot be empty" }
    }

    companion object {
        val EMPTY_SCHEDULE = Schedule("Empty", emptyList())
    }
}

fun computeSchedules(subjects: List<Subject>): List<Schedule> {
    var schedules = listOf<Schedule>()
    for (ls in 40..42) for (pc in 40..43) for (lae in 40..43) for (sisInf in 40..42) {
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
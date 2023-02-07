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
    for (ls in 41..42) for (pc in 41..42) for (lae in 41..42) for (sisInf in 41..42) {
        val newSchedule = Schedule(
            label = "$ls,$pc,$lae,$sisInf",
            subjects = listOf(
                subjects.first { it.name == "LAE" && it.classId == lae },
                subjects.first { it.name == "LS" && it.classId == ls },
                subjects.first { it.name == "PC" && it.classId == pc },
                subjects.first { it.name == "SisInf" && it.classId == sisInf }
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
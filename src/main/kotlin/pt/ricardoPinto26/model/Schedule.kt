package pt.ricardoPinto26.model

data class Schedule(val label: String, val subjects: List<Subject>) {
    init {
        require(label.isNotEmpty()) { "Label ($label) cannot be empty" }
    }

    companion object {
        val EMPTY_SCHEDULE = Schedule("Empty", emptyList())
    }
}

fun List<Subject>.computeSchedules(): List<Schedule> {
    if (this.isEmpty()) return emptyList()
    if (this.size == 1) return listOf(Schedule(this.first().classId.toString(), this))
    var schedules = listOf<Schedule>()
    var list = listOf<String>()
    val _range = mutableListOf<Int>()
    this.forEach {
        if (!list.contains(it.name))
            list = list + it.name
        if (it.classId !in _range)
            _range.add(it.classId)
    }
    val range = _range.toList().sorted()
    val valueList = list.map { range.first() }.toMutableList()
    var currentIndex = list.lastIndex
    var wentBack = false
    var firstIteration = true


    while (true) {
        if (valueList.all { it == range.last() } && !firstIteration) break
        while (valueList[currentIndex] == range.last() && !firstIteration) {
            currentIndex--
            wentBack = true
        }
        if (!firstIteration)
            valueList[currentIndex] = range.elementAt(range.indexOf(valueList[currentIndex]) + 1)
        else
            firstIteration = false
        if (wentBack) {
            var tempIndex = list.lastIndex
            while (tempIndex != currentIndex) {
                valueList[tempIndex] = range.first()
                tempIndex--
            }
            currentIndex = list.lastIndex
            wentBack = false
        }
        run {
            val newSchedule = Schedule(
                label = valueList.toString(),
                subjects = valueList.mapIndexed { index, value ->
                    this.firstOrNull { it.name.contains(list[index]) && it.classId == value } ?: return@run
                }
            )
            if (newSchedule.subjects.all { sub1 ->
                    newSchedule.subjects.all { sub2 ->
                        sub1 == sub2 || sub1.isCompatible(
                            sub2
                        )
                    }
                }) {
                schedules = schedules + newSchedule
            }
        }
    }
    return schedules
}
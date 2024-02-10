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
    if(this.isEmpty()) return emptyList()
    if(this.size == 1) return listOf(Schedule(this.first().classId.toString(), this))

    var subjectsByName: List<List<Subject>> = listOf()
    this.forEach {
        subjectsByName = if (subjectsByName.any { list -> list.any { sub -> sub.name == it.name } })
            subjectsByName.map { list ->
                if (list.any { sub -> sub.name == it.name }) list.plusElement(it)
                else list
            }
        else subjectsByName.plusElement(listOf(it))

    }
    fun cartesianProductWithCompatibilityCheck(
        lists: List<List<Subject>>,
        currentCombination: List<Subject> = emptyList(),
        result: MutableList<List<Subject>> = mutableListOf()
    ): List<List<Subject>> {
        if (lists.isEmpty()) {
            if (currentCombination.isCompatible()) {
                result.add(currentCombination)
            }
            return result
        }

        val firstList = lists.first()
        val remainingLists = lists.drop(1)

        for (element in firstList) {
            val newCombination = currentCombination + element
            cartesianProductWithCompatibilityCheck(remainingLists, newCombination, result)
        }

        return result
    }

    return cartesianProductWithCompatibilityCheck(subjectsByName).map {
        Schedule(it.joinToString { sub -> sub.classId.toString() }, it)
    }
}

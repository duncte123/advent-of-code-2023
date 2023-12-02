import me.duncte123.adventofcode.DayTwo
import me.duncte123.adventofcode.partial.AbstractSolution
import java.util.*

val days = listOf(
    DayOne::class,
    DayTwo::class,
)

fun getDayIndex(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1
}

fun main(args: Array<String>) {
//    val useTestInput = args.isNotEmpty() && "--use-test-input" == args[0]
    val useTestInput = false
    val day = getDayIndex()
    val dayClazz = days[day]
    val instance: AbstractSolution = dayClazz.constructors.first().call()

    val selectedInput = if (useTestInput) instance.getTestInput() else instance.getInput()
    val output = instance.run(selectedInput)

    println("Day ${day + 1}: $output")
}

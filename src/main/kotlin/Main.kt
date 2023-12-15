import me.duncte123.adventofcode.*
import me.duncte123.adventofcode.partial.AbstractSolution
import java.util.*

val days = listOf(
    DayOne::class,
    DayTwo::class,
    DayThree::class,
    DayFour::class,
    DayFive::class,
    DaySix::class,
    DaySeven::class,
    DayEight::class,
    DayNine::class,
)

fun getDayIndex(): Int {
//    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1
    return days.size - 1
}

fun main(args: Array<String>) {
    val useTestInput = args.isNotEmpty() && "--use-test-input" == args[0]
    val day = getDayIndex()
    val dayClazz = days[day]
    val instance: AbstractSolution = dayClazz.constructors.first().call()
    val selectedInput = if (useTestInput) instance.getTestInput() else instance.getInput()

    val start = System.currentTimeMillis()

    val output = instance.run(selectedInput)

    val end = System.currentTimeMillis()

    println("Day ${day + 1}: $output (Took ${end - start}ms)")
}

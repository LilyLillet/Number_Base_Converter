import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun main() {
    launch()
}

fun launch() {
    var a = true
    var b = true
    while (a) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val input = readLine()!!
        if (input != "/exit") {
            var base = input.split(" ").map { it.toInt() }.toMutableList()
            val sourceBase = base[0]
            val targetBase = base[1]
            b = true
            while (b) {
                println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
                val number = readLine()!!
                if (number != "/back") {
                    if (!number.contains(".")) {
                        val value = conversionIntegerToDecimal(number, sourceBase)
                        println("Conversion result: ${convertToOtherSystems(value, targetBase)}")
                    } else {
                        val value = conversionFractionalToDecimal(number, sourceBase)
                        println("Conversion result: ${conversionFractionalToOtherSystems(value, targetBase)}")
                    }
                } else b = false
            }
        } else if (input == "/exit") a = false
    }
}

fun convertToOtherSystems(x: BigInteger, y: Int): String {
    val list = mutableListOf<String>(
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )
    var i = x
    var hex = ""
    while (i > BigInteger.ZERO) {
        val index = i.mod(y.toBigInteger())
        hex += list[index.toInt()]
        i /= y.toBigInteger()
    }
    return hex.reversed()
}

fun conversionIntegerToDecimal(x: String, y: Int): BigInteger {
    return BigInteger(x, y)
}

fun conversionFractionalToDecimal(x: String, y: Int): BigDecimal {
    val list = mutableListOf<Char>(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    )
    val integerPart = conversionIntegerToDecimal(x.substringBefore("."), y)
    val fractionalPart = x.substringAfter(".").uppercase()
    val base = y.toBigDecimal()
    var j = 0
    var result = BigDecimal.ZERO
    j = 1
    for (n in fractionalPart.indices) {
        val pow = base.pow(j)
        val char = fractionalPart[n]
        val index = list.indexOf(char)
        val m = index.toBigDecimal()
        j++
        if (m != BigDecimal.ZERO) {
            val sum = m.divide(pow, 10, RoundingMode.HALF_EVEN)
            result = result.plus(sum)
        } else result += BigDecimal.ZERO
    }
    return integerPart.toBigDecimal().plus(result)
}

fun conversionFractionalToOtherSystems(x: BigDecimal, y: Int): String {
    val list = mutableListOf<String>(
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )
    val integerPart = convertToOtherSystems(x.setScale(0, RoundingMode.DOWN).toBigInteger(), y)
    var i = x.remainder(BigDecimal.ONE)
    var fractalPart = "."
    for (c in 0 until 5) {
        var resultOfMultiply = i * y.toBigDecimal()
        if (resultOfMultiply >= BigDecimal.ZERO) {
            val index = resultOfMultiply.setScale(0, RoundingMode.DOWN).toInt()
            fractalPart += list[index]
            i = resultOfMultiply.minus(index.toBigDecimal())
        }
    }
    return integerPart + fractalPart
}


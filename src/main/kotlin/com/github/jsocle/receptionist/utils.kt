package com.github.jsocle.receptionist

import java.text.SimpleDateFormat
import java.util.*

class DateRange(override val start: Date, override val end: Date) : ClosedRange<Date>, Iterable<Date> {
    override val endInclusive: Date
        get() = end

    override fun iterator(): Iterator<Date> {
        return object : Iterator<Date> {
            private var current = this@DateRange.start.add(Calendar.DATE, -1)

            override fun next(): Date {
                current = current.add(Calendar.DATE, 1)
                return current
            }

            override fun hasNext(): Boolean = current < end
        }
    }
}

operator fun Date.rangeTo(end: Date): ClosedRange<Date> = DateRange(this, end)
fun <T> Iterable<T>.forEachChunked(size: Int, operation: (List<T>) -> Unit) {
    var list: MutableList<T>? = null
    forEachIndexed { i, t ->
        if (i % size == 0) {
            list = ArrayList(size)
        }

        list!!.add(t)

        if (list!!.size == size) {
            operation(list!!)
            list = null
        }
    }
    if (list != null) {
        operation(list!!)
    }
}

val Date.dayOfWeek: Int
    get() = Calendar.getInstance().let { it.time = this; it.get(Calendar.DAY_OF_WEEK) }

fun Date.add(field: Int, amount: Int): Date {
    return Calendar.getInstance().let {
        it.time = this
        it.add(field, amount)
        it.time
    }
}

val Date.dayOfMonth: Int
    get() = Calendar.getInstance().let { it.time = this; it.get(Calendar.DAY_OF_MONTH) }
val Date.midnight: Date
    get() = Calendar.getInstance().let {
        it.time = this
        it.set(Calendar.HOUR_OF_DAY, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        it.set(Calendar.MILLISECOND, 0)
        it.time
    }

fun Date.toString(format: String): String = SimpleDateFormat(format).format(this)

fun dateOf(year: Int, month: Int, date: Int): Date {
    return Calendar.getInstance().let {
        it.time = Date()
        it.set(year, month, date)
        it.time
    }.midnight
}

val Date.previousMonth: Date get() = add(Calendar.MONTH, -1)
val Date.nextMonth: Date get() = add(Calendar.MONTH, 1)

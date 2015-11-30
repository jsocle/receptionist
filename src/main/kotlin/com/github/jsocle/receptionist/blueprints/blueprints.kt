package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.IntField
import com.github.jsocle.html.elements.Html
import com.github.jsocle.html.extentions.addClass
import com.github.jsocle.receptionist.*
import com.github.jsocle.receptionist.models.Reservation
import com.github.jsocle.requests.handlers.RequestHandler0
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

object mainApp : Blueprint() {
    val index: RequestHandler0<Html> = route("/") { ->
        val form = object : Form() {
            val year by IntField()
            val month by IntField()
        }
        if (form.year.value == null) {
            form.year.value = LocalDate.now().year
        }
        if (form.month.value == null || (form.month.value!! < 0 || form.month.value!! > 12)) {
            form.month.value = LocalDate.now().monthValue
        }
        val currentMonth = dateOf(form.year.value!!, form.month.value!! - 1, 1)

        val startDate = currentMonth.add(Calendar.DATE, Calendar.SUNDAY - currentMonth.dayOfWeek)
        val lastDayOfCurrentMonth = currentMonth.add(Calendar.MONTH, 1).add(Calendar.DATE, -1)
        val lastDate = lastDayOfCurrentMonth.add(Calendar.DATE, Calendar.SATURDAY - lastDayOfCurrentMonth.dayOfWeek)

        val reservations = app.db.session
                .createQuery("from Reservation where user = :user and startAt >= :startDate and startAt < :lastDate")
                .setParameter("user", g.user!!)
                .setParameter("startDate", startDate)
                .setParameter("lastDate", lastDate.add(Calendar.DATE, 1) /* lastDate is inclusiveRange */)
                .list() as List<Reservation>

        // 그냥 Comment 달아서 여기서 부터 UI라고 얘기하세요... ㅋㅋㅋ
        containerLayout {
            div {
                //나쁜 예시 - 그냥 복붓하삼...
                nav {
                    ul(class_ = "pager") {
                        listOf(currentMonth.previousMonth to "←", currentMonth to currentMonth.toString("YYYY-MM"), currentMonth.nextMonth to "→").forEach {
                            li {
                                a(href = index.url("year" to it.first.toString("YYYY"), "month" to it.first.month + 1), text_ = it.second)
                            }
                        }
                    }
                }
            }
            table(class_ = "table") {
                tr {
                    DayOfWeek.values.forEach {
                        th(text_ = it.name)
                    }
                }
                //좋은 예시 - 이렇게 쓰라고....
                DateRange(startDate, lastDate).forEachChunked(7) { weekDates ->
                    tr {
                        weekDates.forEach { date ->
                            td {
                                p(text_ = date.dayOfMonth.toString()) {
                                    addClass(
                                            when (date.dayOfWeek) {
                                                Calendar.SUNDAY -> "text-danger"
                                                Calendar.SATURDAY -> "text-primary"
                                                else -> "text-muted"
                                            }
                                    )
                                }
                                reservations
                                        .filter { it.startAt.midnight == date }
                                        .sortedBy { it.endAt }
                                        .sortedBy { it.startAt }
                                        .forEach {
                                            p(text_ = "${it.startAt.toString("HH:mm")} ~ ${it.endAt.toString("HH:mm")} ${it.user.userId}")
                                        }

                            }
                        }
                    }
                }
            }
        }
    }
}

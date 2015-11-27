package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.IntField
import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.g
import com.github.jsocle.receptionist.layout
import com.github.jsocle.receptionist.models.Reservation
import java.util.*

object mainApp : Blueprint() {
    val index = route("/") { ->
        val form = object : Form() {
            val year by IntField()
            val month by IntField()
        }
        if (form.year.value == null) {
            form.year.value = Date().year
        }
        if (form.month.value == null || (form.month.value!! < 1 || form.month.value!! > 12)) {
            form.month.value = Date().month + 1
        }

        val month = Date(form.year.value!!, form.month.value!! - 1, 1)
        val calendar = Calendar.getInstance()
        calendar.time = month
        calendar.add(Calendar.MONTH, 1)
        val nextMonth = calendar.time

        val reservations = app.db.session
                .createQuery("from Reservation where user = :user and startAt >= :month and startAt < :nextMonth")
                .setParameter("user", g.user!!)
                .setParameter("month", month)
                .setParameter("nextMonth", nextMonth)
                .list() as List<Reservation>
        layout {}
    }
}
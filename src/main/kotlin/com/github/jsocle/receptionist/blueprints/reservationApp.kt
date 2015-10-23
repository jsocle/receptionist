package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.DateField
import com.github.jsocle.form.validators
import com.github.jsocle.form.validators.Required
import com.github.jsocle.hibernate.commit
import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.defaultLayout
import com.github.jsocle.receptionist.formGroup
import com.github.jsocle.receptionist.g
import com.github.jsocle.receptionist.models.Reservation
import com.github.jsocle.request
import com.github.jsocle.requests.Request

object reservationApp : Blueprint() {
    val new_ = route("/new/edit") { -> edit(null) }
    val edit = route("/<id:Int>/edit") { id: Int -> edit(id) }

    fun edit(id: Int?): Any {
        val form = object : Form() {
            val startAt by DateField(format = "yyyy-MM-dd HH:mm").apply { validators.add(Required()) }
            val endAt by DateField(format = "yyyy-MM-dd HH:mm").apply { validators.add(Required()) }
        }

        val reservation: Reservation
        if (id == null) {
            reservation = Reservation().apply { user = g.user!! }
        } else {
            reservation = app.db.session.get(Reservation::class.java, id)
            assert(reservation.user == g.user)
            if (request.method == Request.Method.GET) {
                form.startAt.value = reservation.startAt
                form.endAt.value = reservation.endAt
            }
        }

        if (form.validateOnPost()) {
            app.db.session.beginTransaction()
            reservation.apply { startAt = form.startAt.value!!; endAt = form.endAt.value!! }
            if (reservation.id == null) {
                app.db.session.persist(reservation)
            }
            app.db.session.commit()
            return redirect(edit.url(reservation.id!!))
        }

        return defaultLayout {
            div(class_ = "container") {
                div(class_ = "row") {
                    h3(text_ = "예약")
                    hr()
                    form(method = Request.Method.POST.name, action = if (id == null) new_.url() else edit.url(id)) {
                        formGroup(form.startAt)
                        formGroup(form.endAt)
                        button(type = "submit", class_ = "btn btn btn-primary", text_ = if (id == null) "신청" else "수정")
                    }
                }
            }
        }
    }
}
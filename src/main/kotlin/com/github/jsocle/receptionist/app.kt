package com.github.jsocle.receptionist

import com.github.jsocle.JSocle
import com.github.jsocle.hibernate.Hbm2ddlAuto
import com.github.jsocle.hibernate.Hibernate
import com.github.jsocle.hibernate.HibernateProperties
import com.github.jsocle.receptionist.blueprints.*
import com.github.jsocle.receptionist.models.Reservation
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.request
import org.hibernate.cfg.AvailableSettings
import java.util.*

object app : JSocle(config) {
    val db = Hibernate(
            this,
            HibernateProperties("jdbc:h2:mem:jsocle-receptionist", Hbm2ddlAuto.CreateDrop),
            listOf(User::class, Reservation::class)
    ).apply { properties[AvailableSettings.SHOW_SQL] = "true" }

    init {
        register(mainApp)
        register(loginApp)
        register(signUpApp)
        register(reservationApp, "/reservation")
    }

    override fun onBeforeRequest(): Any? {
        if (g.user == null) {
            if (loginApp !in request.handlerCallStack && signUpApp !in request.handlerCallStack) {
                return redirect(loginApp.login.url())
            }
        }
        return null
    }
}


fun main(args: Array<String>) {
    app.db.session {
        val steve = User(userId = "steve", password = "1").apply { it.persist(this) }
        Reservation().apply {
            user = steve
            startAt = Date()
            endAt = startAt.add(Calendar.HOUR, 1)
            it.persist(this)
        }
        it.flush()
    }
    app.run(config.port)
}

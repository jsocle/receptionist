package com.github.jsocle.receptionist

import com.github.jsocle.JSocle
import com.github.jsocle.hibernate.Hbm2ddlAuto
import com.github.jsocle.hibernate.Hibernate
import com.github.jsocle.hibernate.HibernateProperties
import com.github.jsocle.receptionist.blueprints.loginApp
import com.github.jsocle.receptionist.blueprints.mainApp
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.request
import org.hibernate.cfg.AvailableSettings

object app : JSocle(config) {
    val db = Hibernate(
            this,
            HibernateProperties("jdbc:h2:mem:jsocle-receptionist", Hbm2ddlAuto.Create),
            listOf(User::class)
    ).apply { properties[AvailableSettings.SHOW_SQL] = "true" }

    init {
        register(mainApp)
        register(loginApp)
    }

    override fun onBeforeRequest(): Any? {
        if (g.user == null) {
            if (loginApp !in request.handlerCallStack) {
                return redirect(loginApp.login.url())
            }
        }
        return null
    }
}


fun main(args: Array<String>) {
    app.run(config.port)
}

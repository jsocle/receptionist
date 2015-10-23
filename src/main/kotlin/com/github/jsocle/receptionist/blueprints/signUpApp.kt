package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Form
import com.github.jsocle.form.add
import com.github.jsocle.form.fields.PasswordField
import com.github.jsocle.form.fields.StringField
import com.github.jsocle.form.validators
import com.github.jsocle.form.validators.Required
import com.github.jsocle.hibernate.commit
import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.defaultLayout
import com.github.jsocle.receptionist.formGroup
import com.github.jsocle.receptionist.g
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.requests.Request
import com.github.jsocle.requests.handlers.RequestHandler0

object signUpApp : Blueprint() {
    val signUp: RequestHandler0<Any> = route("/signUp") { ->
        val form = object : Form() {
            val id by StringField().apply {
                validators.add(Required(), {
                    if (!hasErrors) {
                        val user = app.db.session
                                .createQuery("from User where userId = :userId")
                                .setParameter("userId", value)
                                .uniqueResult()
                        if (user != null) {
                            errors.add("사용할 수 없는 아이디 입니다.")
                        }
                    }
                })
            }
            val password by PasswordField().apply { validators.add(Required()) }
            val passwordConfirm by PasswordField().apply {
                validators.add(Required(), {
                    if (!hasErrors) {
                        if (password.value != value) {
                            errors.add("패스워드와 패스워드 확인이 일치 하지 않습니다.")
                        }
                    }
                })
            }
        }

        if (form.validateOnPost()) {
            app.db.session.beginTransaction()
            val user = User(userId = form.id.value!!, password = form.password.value!!)
            app.db.session.persist(user)
            app.db.session.commit()
            g.userId = user.id
            return@route redirect(g.defaultReturnTo)
        }

        defaultLayout(css = listOf("/static/css/login.css")) {
            div(class_ = "container") {
                div(class_ = "row") {
                    div(class_ = "main") {
                        h3(text_ = "Sign Up")
                        form(action = signUp.url(), method = Request.Method.POST.name) {
                            formGroup(form.id)
                            formGroup(form.password)
                            formGroup(form.passwordConfirm)
                            button(type = "submit", class_ = "btn btn btn-primary", text_ = "Sign Up")
                        }
                    }
                }
            }
        }

    }
}


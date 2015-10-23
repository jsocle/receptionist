package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Form
import com.github.jsocle.form.add
import com.github.jsocle.form.fields.PasswordField
import com.github.jsocle.form.fields.StringField
import com.github.jsocle.form.validators
import com.github.jsocle.form.validators.Required
import com.github.jsocle.html.elements.Html
import com.github.jsocle.receptionist.defaultLayout
import com.github.jsocle.receptionist.formGroup
import com.github.jsocle.requests.Request
import com.github.jsocle.requests.handlers.RequestHandler0

object signUpApp : Blueprint() {
    val signUp: RequestHandler0<Html> = route("/signUp") { ->
        val form = object : Form() {
            val id by StringField().apply { validators.add(Required()) }
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

        form.validateOnPost()

        defaultLayout(css = listOf("/static/css/login.css")) {
            div(class_ = "container") {
                div(class_ = "row") {
                    div(class_ = "main") {
                        h3(text_ = "Sign Up")
                        form(action = signUp.url(), method = Request.Method.POST.name()) {
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


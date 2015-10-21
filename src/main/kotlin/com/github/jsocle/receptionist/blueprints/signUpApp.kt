package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Field
import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.PasswordField
import com.github.jsocle.form.fields.StringField
import com.github.jsocle.html.elements.Div
import com.github.jsocle.html.elements.Html
import com.github.jsocle.html.extentions.addClass
import com.github.jsocle.receptionist.defaultLayout
import com.github.jsocle.requests.Request
import com.github.jsocle.requests.handlers.RequestHandler0

fun formGroup(field: Field<*, *>, layout: Div.() -> Unit): Div = Div(class_ = "form-group") {
    if (field.errors.isNotEmpty()) {
        addClass("has-warning")
    }
    layout()
    field.errors.forEach {
        span(class_ = "help-block", text_ = it)
    }
}

object signUpApp : Blueprint() {
    val signUp: RequestHandler0<Html> = route("/signUp") { ->
        val form = object : Form() {
            val id by StringField()
            val password by PasswordField()
        }

        defaultLayout(css = listOf("/static/css/login.css")) {
            div(class_ = "container") {
                div(class_ = "row") {
                    div(class_ = "main") {
                        h3(text_ = "Sign Up")
                        form(action = signUp.url(), method = Request.Method.POST.name()) {
                            +formGroup(form.id) {
                                label(text_ = "Id") { attributes["for"] = form.id.name }
                                +form.id.render { class_ = "form-control"; id = form.id.name }
                            }
                            +formGroup(form.password) {
                                label(text_ = "Password") { attributes["for"] = form.password.name }
                                +form.password.render { class_ = "form-control"; id = form.password.name }
                            }
                            button(type = "submit", class_ = "btn btn btn-primary", text_ = "Sign Up")
                        }
                    }
                }
            }
        }

    }
}


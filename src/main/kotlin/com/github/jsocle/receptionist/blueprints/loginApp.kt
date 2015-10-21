package com.github.jsocle.receptionist.blueprints

import com.github.jsocle.Blueprint
import com.github.jsocle.form.Form
import com.github.jsocle.form.fields.PasswordField
import com.github.jsocle.form.fields.StringField
import com.github.jsocle.html.elements.Html
import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.defaultLayout
import com.github.jsocle.receptionist.g
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.request
import com.github.jsocle.requests.Request
import com.github.jsocle.requests.handlers.RequestHandler0


object loginApp : Blueprint() {
    val login: RequestHandler0<Html> = route("/login") { ->
        val form = object : Form() {
            val id by StringField()
            val password by PasswordField()
        }
        if (request.method == Request.Method.POST) {
            val user = app.db.session
                    .createQuery("from User where userId = :userId and password = :password")
                    .setParameter("userId", form.id.value)
                    .setParameter("password", form.password.value)
                    .uniqueResult() as User?
            if (user != null) {
                g.userId = user.id
            }
        }

        defaultLayout(css = listOf("/static/css/login.css")) {
            div(class_ = "container") {
                div(class_ = "row") {
                    div(class_ = "main") {
                        h3 {
                            +"Please Log In, or "
                            a(href = "#", text_ = "Sign Up")
                        }
                        div(class_ = "row") {
                            div(class_ = "col-xs-6 col-sm-6 col-md-6") {
                                a(href = "#", class_ = "btn btn-lg btn-primary btn-block", text_ = "Facebook")
                            }
                            div(class_ = "col-xs-6 col-sm-6 col-md-6") {
                                a(href = "#", class_ = "btn btn-lg btn-info btn-block", text_ = "Google")
                            }
                        }
                        div(class_ = "login-or") {
                            hr(class_ = "hr-or")
                            span(class_ = "span-or", text_ = "or")
                        }
                        form(action = login.url(), method = Request.Method.POST.name()) {
                            div(class_ = "form-group") {
                                label(text_ = "Username or email") { attributes["for"] = form.id.name }
                                +form.id.render { class_ = "form-control"; id = form.id.name }
                            }
                            div(class_ = "form-group") {
                                a(class_ = "pull-right", href = "#", text_ = "Forgot password?")
                                label(text_ = "Password") { attributes["for"] = form.password.name }
                                +form.password.render { class_ = "form-control"; id = form.password.name }
                            }
                            div(class_ = "checkbox pull-right") {
                                label {
                                    input(type = "checkbox")
                                    +"Remember me"
                                }
                            }
                            button(type = "submit", class_ = "btn btn btn-primary", text_ = " Log In ")
                        }
                    }
                }
            }
        }
    }
}

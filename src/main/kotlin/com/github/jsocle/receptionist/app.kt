package com.github.jsocle.receptionist

import com.github.jsocle.JSocle
import com.github.jsocle.html.elements.Html

fun layout(): Html {
    return Html(lang = "en") {
        head {
            meta { attributes["charset"] = "utf-8" }
            meta(httpEquiv = "X-UA-Compatible", content = "IE=edge")
            meta(name = "description", content = "")
            meta(name = "author", content = "")
            title(text_ = "Starter Template for Bootstrap")
            link(href = "/static/bootstrap-3.3.4-dist/css/bootstrap.min.css", rel = "stylesheet")
        }
        body(style = "padding-top: 50px;") {
            nav(class_ = "navbar navbar-inverse navbar-fixed-top") {
                div(class_ = "container") {
                    div(class_ = "navbar-header") {
                        button(type = "button", class_ = "navbar-toggle collapsed") {
                            data_["toggle"] = "collapse"
                            data_["target"] = "#navbar"
                            span(class_ = "sr-only", text_ = "Toggle navigation")
                            span(class_ = "icon-bar")
                            span(class_ = "icon-bar")
                            span(class_ = "icon-bar")
                        }
                        a(class_ = "navbar-brand", href = "#", text_ = "Project name")
                    }
                    div(id = "navbar", class_ = "collapse navbar-collapse") {
                        ul(class_ = "nav navbar-nav") {
                            li(class_ = "active") {
                                a(href = "#", text_ = "Home")
                            }
                            li {
                                a(href = "#about", text_ = "About")
                            }
                            li {
                                a(href = "#contact", text_ = "Contact")
                            }
                        }
                    }
                }
            }
            div(class_ = "container") {
                div(class_ = "starter-template") {
                    h1(text_ = "Bootstrap starter template")
                    p(class_ = "lead") {
                        +"Use this document as a way to quickly start any new project."
                        br()
                        +"All you get is this text and a mostly barebones HTML document."
                    }
                }
            }
            script(src = "/static/js/vendor/jquery-1.11.3.min.js")
            script(src = "/static/bootstrap-3.3.4-dist/js/bootstrap.min.js")
        }
    }
}

object app : JSocle() {
    init {
        route("/") { ->
            return@route layout()
        }
    }
}


fun main(args: Array<String>) {
    app.run(config.port)
}

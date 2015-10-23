package com.github.jsocle.receptionist

import com.github.jsocle.html.elements.Body
import com.github.jsocle.html.elements.Html
import com.github.jsocle.receptionist.blueprints.mainApp

fun defaultLayout(css: List<String> = listOf(), layout: Body.() -> Unit): Html {
    return Html(lang = "en") {
        head {
            meta { attributes["charset"] = "utf-8" }
            meta(httpEquiv = "X-UA-Compatible", content = "IE=edge")
            meta(name = "description", content = "")
            meta(name = "author", content = "")
            title(text_ = "Receptionist")
            link(href = "/static/bootstrap-3.3.4-dist/css/bootstrap.min.css", rel = "stylesheet")
            css.forEach { link(href = it, rel = "stylesheet") }
        }
        body(style = "padding-top: 50px;") {
            layout()
            script(src = "/static/js/vendor/jquery-1.11.3.min.js")
            script(src = "/static/bootstrap-3.3.4-dist/js/bootstrap.min.js")
        }
    }
}

fun layout(): Html {
    return defaultLayout {
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
                    a(class_ = "navbar-brand", href = mainApp.index.url(), text_ = "방문 예약 시스템")
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
    }
}

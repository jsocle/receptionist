package com.github.jsocle.receptionist

import com.github.jsocle.form.Field
import com.github.jsocle.html.BaseEmptyElement
import com.github.jsocle.html.Node
import com.github.jsocle.html.elements.Div
import com.github.jsocle.html.elements.Element
import com.github.jsocle.html.extentions.addClass

fun Field<*, *>.formControl(): Node {
    return render {
        val e = this
        if (e is BaseEmptyElement) {
            e.addClass("form-control")
            e.attributes["id"] = name
        }
    }
}

fun Element.formGroup(field: Field<*, *>, label: String = field.name): Div = div(class_ = "form-group") {
    if (field.errors.isNotEmpty()) {
        addClass("has-error")
    }
    label(text_ = label) { attributes["for"] = field.name }
    +field.formControl()
    field.errors.forEach {
        span(class_ = "help-block", text_ = it)
    }
}
package com.github.jsocle.receptionist

import com.github.jsocle.receptionist.models.User
import com.github.jsocle.request

object g {
    const val PREFIX = "com.github.jsocle.receptionist.g."

    val user: User?
        get() {
            return request.g.getOrSet(PREFIX + "user") {
                if (userId != null) {
                    app.db.session.createQuery("from User where id = :id").setParameter("id", userId).uniqueResult()
                } else {
                    null
                }
            } as User?
        }

    var userId: Int?
        get() {
            if (PREFIX + "userId" in request.session) {
                return Integer.parseInt(request.session[PREFIX + "userId"] as String)
            }
            return null
        }

        set(userId: Int?) {
            if (userId != null) {
                request.session[PREFIX + "userId"] = userId.toString()
            } else {
                request.session.remove(PREFIX + "userId")
            }
            request.g.remove(PREFIX + "user")
        }
}


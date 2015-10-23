package com.github.jsocle.receptionist.models

import java.util.*
import javax.persistence.*

@Entity
class Reservation(@GeneratedValue @Id val id: Int? = null) {
    @ManyToOne @JoinColumn(nullable = false)
    lateinit var user: User
    @Column(unique = true, nullable = false)
    lateinit var startAt: Date
    @Column(unique = true, nullable = false)
    lateinit var endAt: Date
}
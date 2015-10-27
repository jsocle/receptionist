package com.github.jsocle.receptionist.models

import javax.persistence.*

@Entity
class User(@GeneratedValue @Id val id: Int? = null,
           @Column(nullable = false, unique = true) val userId: String = "",
           @Column(nullable = false) val password: String = "",
           @OneToMany(mappedBy = "user", fetch = javax.persistence.FetchType.LAZY) val reservations: List<Reservation> = listOf())

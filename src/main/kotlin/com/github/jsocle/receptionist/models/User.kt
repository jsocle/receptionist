package com.github.jsocle.receptionist.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(@GeneratedValue @Id val id: Int? = null,
           @Column(nullable = false, unique = true) val userId: String = "",
           @Column(nullable = false) val password: String = "")

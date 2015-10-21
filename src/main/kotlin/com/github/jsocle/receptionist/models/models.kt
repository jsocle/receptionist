package com.github.jsocle.receptionist.models

import javax.persistence.Entity

@Entity
class User(@javax.persistence.GeneratedValue @javax.persistence.Id val id: Int? = null,
           @javax.persistence.Column(nullable = false, unique = true) val userId: String = "",
           @javax.persistence.Column(nullable = false) val password: String = "")

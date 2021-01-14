package com.nononsensecode.simple.domain.model

import javax.persistence.*

@Entity
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val name: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "client")
    val roles: Set<Role>
)
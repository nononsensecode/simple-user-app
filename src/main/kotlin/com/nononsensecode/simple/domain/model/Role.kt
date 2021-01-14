package com.nononsensecode.simple.domain.model

import javax.persistence.*

@Entity
class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val client: Client
)
package com.exam.pg6101.social.media.service.entities

import com.sun.istack.NotNull
import java.time.ZonedDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "users")
class UserEntity(

        @get:NotBlank
        var name: String? = null,

        @get:NotBlank
        var surname: String? = null,

        @get:NotBlank
        var email: String? = null,

        @get:NotNull
        var creationTime: ZonedDateTime,

        @get:Id @get:GeneratedValue(strategy= GenerationType.IDENTITY)
        var id: Long? = null

)
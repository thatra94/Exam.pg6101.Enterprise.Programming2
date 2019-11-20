package com.exam.pg6100.social.media.sevice.entities

import com.sun.istack.NotNull
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class UserEntity(

        @get:NotNull
        var name: String? = null,

        @get:NotNull
        var surname: String? = null,

        @get:NotNull
        var email: String? = null,

        @get:NotNull
        var creationTime: ZonedDateTime,

        @get:Id @get:GeneratedValue
        var id: Long? = null

)
package com.exam.pg6100.social.media.sevice.dto

import com.sun.istack.NotNull
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@ApiModel("User details")
data class UserDto(

        @get:NotNull
        @ApiModelProperty("The name of the user")
        var name: String? = null,

        @get:NotNull
        @ApiModelProperty("The surname of the user")
        var surname: String? = null,

        @get:NotNull
        @ApiModelProperty("The email address of the user")
        var email: String? = null,

        @get:NotNull
        @ApiModelProperty("The creation time of the user")
        var creationTime: ZonedDateTime,

        @get:Id @get:GeneratedValue
        @ApiModelProperty("The unique id that identifies this user")
        var id: Long? = null



        )
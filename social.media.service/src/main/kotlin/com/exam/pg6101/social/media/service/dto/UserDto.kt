package com.exam.pg6101.social.media.service.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel("User details")
data class UserDto(

        @ApiModelProperty("The name of the user")
        var name: String? = null,

        @ApiModelProperty("The surname of the user")
        var surname: String? = null,

        @ApiModelProperty("The email address of the user")
        var email: String? = null,

        @ApiModelProperty("The creation time of the user")
        var creationTime: ZonedDateTime? = null,

        @ApiModelProperty("The unique id that identifies this user")
        var id: String? = null

)
package com.exam.pg6100.social.media.sevice.dto

import com.exam.pg6100.social.media.sevice.entities.UserEntity

class UserConverter {

    companion object {

        fun transform(entity: UserEntity): UserDto {

            return UserDto(
                    name = entity.name,
                    surname = entity.surname,
                    email = entity.email,
                    creationTime = entity.creationTime,
                    id = entity.id?.toString()
                    )
        }

        fun transform(entities: Iterable<UserEntity>): List<UserDto> {
            return entities.map { transform(it) }
        }
    }
}
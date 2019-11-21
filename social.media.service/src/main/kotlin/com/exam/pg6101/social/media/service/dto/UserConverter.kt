package com.exam.pg6101.social.media.service.dto

import com.exam.pg6101.social.media.service.entities.UserEntity

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
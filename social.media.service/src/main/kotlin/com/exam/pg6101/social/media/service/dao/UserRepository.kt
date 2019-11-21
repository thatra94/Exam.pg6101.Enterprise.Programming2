package com.exam.pg6101.social.media.service.dao

import com.exam.pg6101.social.media.service.entities.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import javax.persistence.EntityManager

@Repository
interface UserRepository : CrudRepository<UserEntity, Long>, UserRepositoryCustom {

    fun findAllByName(name: String): Iterable<UserEntity>
    fun findAllBySurname(surname: String): Iterable<UserEntity>

    fun findAllByNameAndSurname(name: String, surname: String): Iterable<UserEntity>


}

@Transactional
interface UserRepositoryCustom {

    fun createUser(name: String, surname: String, email: String): Long

    fun updateEmail(userId: Long, email: String): Boolean

    fun update(userId: Long,
               name: String,
               surname: String,
               email: String,
               creationTime: ZonedDateTime): Boolean
}

@Repository
@Transactional
class UserRepositoryImpl : UserRepositoryCustom {

    /*
        To operate manually on the database with JPA, we can inject a reference
        to EntityManager.

        Note the use of "lateinit".
        The field is marked as non-nullable, but we do not initialize it.
        This would lead to a compilation error.
        However, the field will be assigned with dependency injection by Spring
        once the constructor call is completed.
        So, here were are just telling the Kotlin compiler that this field "em"
        is not null, but we are going to initialize it at a later stage.
     */

    @Autowired
    private lateinit var em: EntityManager

    override fun createUser(name: String, surname: String, email: String): Long {
        val entity = UserEntity(name, surname, email, ZonedDateTime.now())
        em.persist(entity)
        return entity.id!!
    }

    override fun updateEmail(userId: Long, email: String): Boolean {
        val user = em.find(UserEntity::class.java, userId) ?: return false
        user.email = email
        return true
    }

    override fun update(userId: Long,
                        name: String,
                        surname: String,
                        email: String,
                        creationTime: ZonedDateTime): Boolean {
        val user = em.find(UserEntity::class.java, userId) ?: return false
        user.name = name
        user.surname = surname
        user.email = email
        user.creationTime = creationTime
        return true
    }
}
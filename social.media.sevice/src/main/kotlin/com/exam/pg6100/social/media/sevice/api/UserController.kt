package com.exam.pg6100.social.media.sevice.api

import com.exam.pg6100.social.media.sevice.dao.UserRepository
import com.exam.pg6100.social.media.sevice.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    lateinit var repository: UserRepository
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): UserDto? = repository.findById(id)
    @GetMapping
    fun findAll(): List<UserDto> = repository.findAll()
    @PostMapping
    fun add(@RequestBody user: UserDto): UserDto = repository.save(user)
    @PutMapping
    fun update(@RequestBody user: UserDto): UserDto = repository.update(user)
    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: Int): Boolean = repository.removeById(id)
}
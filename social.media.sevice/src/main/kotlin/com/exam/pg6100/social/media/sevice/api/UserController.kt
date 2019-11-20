package com.exam.pg6100.social.media.sevice.api

import com.exam.pg6100.social.media.sevice.dao.UserRepository
import com.exam.pg6100.social.media.sevice.dto.UserConverter
import com.exam.pg6100.social.media.sevice.dto.UserDto
import com.google.common.base.Throwables
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

const val ID_PARAM = "The numeric id of the user"


@Api(value = "/news", description = "Handling of creating and retrieving news")

@RestController
@RequestMapping("/users")
class UserController {

    /*
        Main HTTP verbs/methods:
        GET: get the resource specified in the URL
        POST: send data, creating a new resource
        PUT: update a resource
        PATCH: partial update
        DELETE: delete the resource

        Common status codes:
        200: OK
        201: Resource created
        204: Done, nothing to return
        400: General user error, bad request
        404: Resource not found
        409: Conflict with current state
        500: Server error
     */

    /*
        Note: here inputs (what is in the method parameters) and outputs will
        be automatically processed by Spring using its own JSON library (eg Jackson).
        So, when we have
        "List<NewsDto>"
        as return value, Spring will automatically marshall it into JSON
     */


    @Autowired
    private lateinit var crud: UserRepository

    /*
        request URL parameters are in the form

        ?<name>=<value>&<name>=<value>&...

        for example

        /news?country=Norway&authordId=foo

        So here we ll have a single endpoint for getting "news", where
        optional filtering on "country" and "authorId" will be based on
        URL parameters, and not different endpoints
     */
    @ApiOperation("Get all the Users")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun get(@ApiParam("The name of the User")
            @RequestParam("name", required = false)
            name: String?,
            //
            @ApiParam("The surname of the User")
            @RequestParam("surname", required = false)
            surname: String?

    ): ResponseEntity<List<UserDto>> {

        /*
            s.isNullOrBlank() might look weird when coming from Java...
            I mean, if a string "s" is null, wouldn't calling (any) method
            on it lead to a NPE???
            This does not happen based on how kotlin code is compiled (you
            can look into the source code of isNullOrBlank to see how exactly
            this is achieved, eg by inlining and specifying the method can
            be called on nullable objects)
         */

        val list = if (name.isNullOrBlank() && surname.isNullOrBlank()) {
            crud.findAll()
        } else if (!name.isNullOrBlank() && !surname.isNullOrBlank()) {
            crud.findAllByNameAndSurname(name, surname)
        } else if (!name.isNullOrBlank()) {
            crud.findAllByName(name)
        } else {
            crud.findAllBySurname(surname!!)
        }
        /*
            Note: here we return a 200 OK even in the case of no data,
            as we still return an [] array (albeit empty).
            Returning a 404 in such a case would be wrong, as the collection
            does exist, even if it is empty.
         */
        return ResponseEntity.ok(UserConverter.transform(list))
    }


    @ApiOperation("Create a User")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(code = 201, message = "The id of newly created User")
    fun createNews(
            @ApiParam("User name, surname and email. Should not specify id or creation time")
            @RequestBody
            dto: UserDto)
            : ResponseEntity<Long> {

        if (!dto.id.isNullOrEmpty()) {
            //Cannot specify id for a newly generated news
            return ResponseEntity.status(400).build()
        }

        if (dto.creationTime != null) {
            //Cannot specify creationTime for a newly generated news
            return ResponseEntity.status(400).build()
        }

        if (dto.name == null || dto.surname == null || dto.email == null) {
            return ResponseEntity.status(400).build()
        }

        val id: Long?
        try {
            id = crud.createUser(dto.name!!, dto.surname!!, dto.email!!)
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(201).body(id)
    }


    @ApiOperation("Get a single User specified by id")
    @GetMapping(path = ["/{id}"])
    fun getNews(@ApiParam(ID_PARAM)
                @PathVariable("id")
                pathId: String?)
            : ResponseEntity<UserDto> {

        val id: Long
        try {
            id = pathId!!.toLong()
        } catch (e: Exception) {
            /*
                invalid id. But here we return 404 instead of 400,
                as in the API we defined the id as string instead of long
             */
            return ResponseEntity.status(404).build()
        }

        val entity = crud.findById(id).orElse(null) ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(UserConverter.transform(entity))
    }


    @ApiOperation("Update an existing User")
    @PutMapping(path = ["/{id}"], consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun update(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            pathId: String,
            //
            @ApiParam("The user that will replace the old one. Cannot change its id though.")
            @RequestBody
            dto: UserDto
    ): ResponseEntity<Any> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            /*
                invalid id. But here we return 404 instead of 400,
                as in the API we defined the id as string instead of long
             */
            return ResponseEntity.status(404).build()
        }

        if (dto.id != pathId) {
            // Not allowed to change the id of the resource (because set by the DB).
            // In this case, 409 (Conflict) sounds more appropriate than the generic 400
            return ResponseEntity.status(409).build()
        }

        if (!crud.existsById(id)) {
            //Here, in this API, made the decision to not allow to create a news with PUT.
            // So, if we cannot find it, should return 404 instead of creating it
            return ResponseEntity.status(404).build()
        }

        if (dto.name == null || dto.surname == null || dto.email == null || dto.creationTime == null) {
            return ResponseEntity.status(400).build()
        }

        try {
            crud.update(id, dto.name!!, dto.surname!!, dto.email!!, dto.creationTime!!)
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(204).build()
    }


    @ApiOperation("Update the email content of an existing news")
    @PutMapping(path = ["/{id}/email"], consumes = [(MediaType.TEXT_PLAIN_VALUE)])
    fun updateText(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            id: Long?,
            //
            @ApiParam("The new email which will replace the old one")
            @RequestBody
            email: String
    ): ResponseEntity<Any> {
        if (id == null) {
            return ResponseEntity.status(400).build()
        }

        if (!crud.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        try {
            crud.updateEmail(id, email)
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(204).build()
    }


    @ApiOperation("Delete a user with the given id")
    @DeleteMapping(path = ["/{id}"])
    fun delete(@ApiParam(ID_PARAM)
               @PathVariable("id")
               pathId: String?): ResponseEntity<Any> {

        val id: Long
        try {
            id = pathId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (!crud.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        crud.deleteById(id)
        return ResponseEntity.status(204).build()
    }

}


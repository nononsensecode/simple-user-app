package com.nononsensecode.simple.infrastructure.hibernate.user

import com.nononsensecode.simple.domain.model.IUserRepository
import com.nononsensecode.simple.domain.model.User
import com.nononsensecode.simple.infrastructure.DataAccessException
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager

private val logger = KotlinLogging.logger {  }

@Repository
class UserRepositoryImpl(
    val entityManager: EntityManager
): IUserRepository {
    override fun findAll(): List<User> {
        return try {
            val query = entityManager.createQuery("SELECT u FROM User u", User::class.java)
            query.resultList
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Unknown exception occurred while accessing list of users", e)
        }
    }

    override fun findUserById(id: UUID): User? {
        return try {
            entityManager.find(User::class.java, id)
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Unknown exception occurred while accessing user with id: $id", e)
        }
    }

    override fun findUserByEmail(email: String): User? {
        return try {
            val query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User::class.java)
            query.setParameter("email", email)
            query.maxResults = 1
            query.resultList.firstOrNull()
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Unknown exception while finding user with email $email", e)
        }
    }

    override fun findUserByUsername(username: String): User? {
        return try {
            val query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User::class.java)
            query.setParameter("username", username)
            query.maxResults = 1
            query.resultList.firstOrNull()
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Unknown exception while finding user with username $username", e)
        }
    }

    override fun getUsersCount(): Int {
        return try {
            val query = entityManager.createQuery("SELECT COUNT(u) FROM User u")
            query.singleResult as Int
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Unknown exception while counting total users", e)
        }
    }

    override fun getUsers(pageable: Pageable): Page<User> {
        return try {
            val countQuery = entityManager.createQuery("SELECT u FROM User u")
            val count = countQuery.singleResult as Long
            val query = entityManager
                .createQuery("SELECT u FROM User u", User::class.java)
                .setFirstResult(pageable.pageSize * pageable.pageNumber)
                .setMaxResults(pageable.pageSize)
            PageImpl(query.resultList, pageable, count)
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Exception occurred while fetching all users in a pageable manner", e)
        }
    }

    override fun findPagedUsersBySearchString(searchString: String, pageable: Pageable): Page<User> {
        return try {
            val countQuery = entityManager.createQuery("""
                SELECT COUNT(u) FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR
                 LOWER(u.email) LIKE LOWER(CONCAT('%', :searchString, '%'))
            """.trimIndent())
            countQuery.setParameter("searchString", searchString)
            val count = countQuery.singleResult as Long

            val query = entityManager.createQuery("""
                SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR
                 LOWER(u.email) LIKE LOWER(CONCAT('%', :searchString, '%'))
            """.trimIndent(), User::class.java)
                .setFirstResult(pageable.pageSize * pageable.pageNumber)
                .setMaxResults(pageable.pageSize)
            query.setParameter("searchString", searchString)
            val users = query.resultList
            PageImpl(users, pageable, count)
        } catch(e: Exception) {
            logger.error { e }
            throw DataAccessException("Exception occurred while searching for users in pageable manner with search string $searchString", e)
        }
    }

    override fun findUsersBySearchString(searchString: String): List<User> {
        return try {
            val query = entityManager.createQuery("""
                SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR
                 LOWER(u.email) LIKE LOWER(CONCAT('%', :searchString, '%'))
            """.trimIndent(), User::class.java)
            query.setParameter("searchString", searchString)
            query.resultList
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Exception occurred while searching for users with search string $searchString", e)
        }
    }

    override fun findUsersBySearchString(searchString: String, pageable: Pageable): List<User> {
        return try {
            val countQuery = entityManager.createQuery("""
                SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR
                 LOWER(u.email) LIKE LOWER(CONCAT('%', :searchString, '%'))
            """.trimIndent())
            val count = countQuery.singleResult as Long
            entityManager.createQuery("""
                SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR
                 LOWER(u.email) LIKE LOWER(CONCAT('%', :searchString, '%'))
            """.trimIndent(), User::class.java)
                .setFirstResult(pageable.pageNumber * pageable.pageSize)
                .setMaxResults(pageable.pageSize)
                .setParameter("searchString", searchString)
                .resultList
        } catch (e: Exception) {
            logger.error { e }
            throw DataAccessException("Exception occurred while fetching users in a pageable manner with search string $searchString", e)
        }
    }
}
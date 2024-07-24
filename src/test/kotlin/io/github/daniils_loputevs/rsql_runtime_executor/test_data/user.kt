package io.github.daniils_loputevs.rsql_runtime_executor.test_data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime


val USER_DEFAULT = User(
    id = 1,
    name = "John Doe",
    isActive = true,
    score = 95.5,
    age = 30,
    height = 5.9f,
    salary = 50000L,
    character = 'A',
    shortValue = 1,
    tokenType = "UUID_token" ,
    dateOfBirth = LocalDate.of(1990, 1, 1),
    timeOfBirth = LocalTime.of(12, 0),
    registeredAt = LocalDateTime.now(),
    offsetDateTime = OffsetDateTime.now(),
    offsetTime = OffsetTime.now(),
    zonedDateTime = ZonedDateTime.now(),
    roles = listOf(UserRole.ADMIN, UserRole.USER),
    attributes = mapOf("email" to "john.doe@example.com", "phone" to "1234567890", "UUID_token" to "AAAA-BBBB-CCCC-DDDD-2016-06-01"),
    loginInfo = LoginInfo(LocalDateTime.of(2024, 6, 1, 16, 15, 0), 117),
)

open class User(
    id: Int,
    override val name: String,
    isActive: Boolean,
    score: Double,
    val age: Byte,
    val height: Float,
    val salary: Long,
    val character: Char,
    val shortValue: Short,
    val tokenType : String,
    val dateOfBirth: LocalDate,
    val timeOfBirth: LocalTime,
    registeredAt: LocalDateTime,
    val offsetDateTime: OffsetDateTime,
    val offsetTime: OffsetTime,
    val zonedDateTime: ZonedDateTime,
    val roles: List<UserRole>,
    val attributes: Map<String, Any>,
    val loginInfo: LoginInfo,
) : WithName, BaseUser(
    id,
    isActive,
    score,
    registeredAt
)


enum class UserRole {
    ADMIN, USER, GUEST
}

open class RegisteredUser(
    val registeredAt: LocalDateTime,
)

open class BaseUser(
    val id: Int,
    val isActive: Boolean,
    val score: Double,
    registeredAt: LocalDateTime,
) : RegisteredUser(registeredAt)

interface WithName {
    val name: String
}


class LoginInfo(val lastLogin: LocalDateTime, val count: Int)

package kopringstudy.kopringstudy.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kopringstudy.kopringstudy.converter.RoleConverter
import kopringstudy.kopringstudy.member.domain.utli.PasswordUtil
import kopringstudy.kopringstudy.exception.exception.MemberCustomException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Entity
class Member private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
    @Column(unique = true) val identity: String,
    @Column(unique = true) val email: String,
    var pw: String,
    var name: String,
    var age: Int,
    @Convert(converter = RoleConverter::class) var auth: Role
) : UserDetails {

    companion object {
        private fun createIdentity(): String = UUID.randomUUID().toString()

        fun create(email: String, pw: String, name: String, age: Int): Member {
            val adminEmail = "admin@gmail.com"

            return Member(
                null,
                createIdentity(),
                email,
                PasswordUtil.encodePassword(pw),
                name,
                age,
                if (email == adminEmail) Role.ADMIN else Role.MEMBER
            )
        }
    }

    fun updatePw(newPassword: String, oldPassword: String) {
        if (!PasswordUtil.isMatchPassword(oldPassword, this.pw)) throw MemberCustomException("비밀번호를 틀렸습니다.")
        this.pw = PasswordUtil.encodePassword(newPassword)
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateAge() = this.age++

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authList = arrayListOf<GrantedAuthority>()
        authList.add(SimpleGrantedAuthority(auth.auth))
        return authList
    }

    override fun getPassword(): String {
        return pw
    }

    override fun getUsername(): String {
        return identity
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
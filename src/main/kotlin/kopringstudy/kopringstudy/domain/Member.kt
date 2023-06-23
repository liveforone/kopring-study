package kopringstudy.kopringstudy.domain

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kopringstudy.kopringstudy.converter.RoleConverter
import kopringstudy.kopringstudy.domain.utli.PasswordUtil
import kopringstudy.kopringstudy.exception.exception.MemberCustomException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Entity
class Member private constructor(
    email:String,
    pw:String,
    name:String,
    age:Int,
    auth:Role
):UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null

    @Column(unique = true)
    var identity:String = createIdentity()

    var email:String = email

    var pw:String = PasswordUtil.encodePassword(pw)

    var name:String = name

    var age:Int = age

    @Convert(converter = RoleConverter::class)
    var auth:Role = auth

    companion object {
        fun create(email: String, pw: String, name:String, age: Int): Member {
            val adminEmail = "admin@gmail.com"
            return Member(email, pw, name, age, if (email == adminEmail) Role.ADMIN else Role.MEMBER)
        }
    }

    private fun createIdentity():String {
        return UUID.randomUUID().toString()
    }

    fun updatePw(password: String, oldPassword:String) {
        if (!PasswordUtil.isMatchPassword(oldPassword, this.pw)) throw MemberCustomException("비밀번호를 틀렸습니다.")
        this.pw = PasswordUtil.encodePassword(password)
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateAge() = age++

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
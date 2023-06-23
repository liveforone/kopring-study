package kopringstudy.kopringstudy.service.command

import kopringstudy.kopringstudy.domain.Member
import kopringstudy.kopringstudy.domain.Role
import kopringstudy.kopringstudy.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService @Autowired constructor(private val memberRepository: MemberRepository) : UserDetailsService{

    override fun loadUserByUsername(email: String): UserDetails {
        return createUserDetails(memberRepository.findOneByEmail(email))
    }

    private fun createUserDetails(member: Member): UserDetails {
        return if (member.auth == Role.ADMIN) {
            User.builder()
                .username(member.identity)
                .password(member.password)
                .roles("ADMIN")
                .build()
        } else {
            User.builder()
                .username(member.identity)
                .password(member.password)
                .roles("MEMBER")
                .build()
        }
    }
}
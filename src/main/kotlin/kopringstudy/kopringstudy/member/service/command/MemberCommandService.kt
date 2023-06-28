package kopringstudy.kopringstudy.member.service.command

import kopringstudy.kopringstudy.member.domain.Member
import kopringstudy.kopringstudy.member.dto.update.ChangePassword
import kopringstudy.kopringstudy.member.dto.request.LoginRequest
import kopringstudy.kopringstudy.member.dto.request.SignupRequest
import kopringstudy.kopringstudy.jwt.JwtTokenProvider
import kopringstudy.kopringstudy.jwt.TokenInfo
import kopringstudy.kopringstudy.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class MemberCommandService @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun createMember(signupRequest: SignupRequest) {
        Member.create(email = signupRequest.email!!, pw = signupRequest.pw!!, name = signupRequest.name!!, age = signupRequest.age!!)
            .also {
                memberRepository.save(it)
            }
    }

    fun login(loginRequest: LoginRequest): TokenInfo {
        val member = memberRepository.findOneByEmail(loginRequest.email!!)

        //시큐리티에게 로그인 위임
        authenticationManagerBuilder.also {
            it.`object`.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.pw)
            )
        }

        return jwtTokenProvider.generateToken(member)
    }

    fun updatePw(changePassword: ChangePassword, identity:String) {
        memberRepository.findOneByIdentity(identity).also {
            it.updatePw(newPassword = changePassword.password!!, changePassword.oldPassword!!)
        }
    }
}
package kopringstudy.kopringstudy.service.command

import kopringstudy.kopringstudy.domain.Member
import kopringstudy.kopringstudy.dto.ChangePassword
import kopringstudy.kopringstudy.dto.LoginRequest
import kopringstudy.kopringstudy.dto.MemberRequest
import kopringstudy.kopringstudy.jwt.JwtTokenProvider
import kopringstudy.kopringstudy.jwt.TokenInfo
import kopringstudy.kopringstudy.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class MemberCommandService @Autowired constructor(
    private val memberRepository:MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun createMember(requestDto:MemberRequest) {
        Member.create(requestDto.email!!, requestDto.pw!!, requestDto.name!!, requestDto.age!!)
            .also {
                memberRepository.save(it)
            }
    }

    fun login(loginRequest:LoginRequest): TokenInfo {
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
            it.updatePw(changePassword.password!!, changePassword.oldPassword!!)
        }
    }
}
package kopringstudy.kopringstudy.service.command

import jakarta.persistence.EntityManager
import kopringstudy.kopringstudy.member.dto.request.LoginRequest
import kopringstudy.kopringstudy.member.dto.request.SignupRequest
import kopringstudy.kopringstudy.member.service.command.MemberCommandService
import kopringstudy.kopringstudy.member.service.query.MemberQueryService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class MemberCommandServiceTest @Autowired constructor(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService,
    private val entityManager: EntityManager
) {

    @Test
    @Transactional
    fun createMemberTest() {
        //given
        val email = "test_create@gmail.com"
        val pw = "1234"
        val name = "createTest"
        val age = 20
        val signupRequest = SignupRequest(email, pw, name, age)

        //when
        memberCommandService.createMember(signupRequest)
        entityManager.flush()
        entityManager.clear()

        //then
        Assertions.assertThat(memberQueryService.getMemberByEmail(email).name)
            .isEqualTo(name)
    }

    @Test
    @Transactional
    fun loginTest() {
        //given
        val email = "test_login@gmail.com"
        val pw = "1234"
        val name = "loginTest"
        val age = 21
        val signupRequest = SignupRequest(email, pw, name, age)
        memberCommandService.createMember(signupRequest)
        entityManager.flush()
        entityManager.clear()

        //when
        val loginRequest = LoginRequest(email, pw)
        val tokenInfo = memberCommandService.login(loginRequest)

        //then
        Assertions.assertThat(tokenInfo)
            .isNotNull
    }
}
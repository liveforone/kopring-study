package kopringstudy.kopringstudy.domain

import kopringstudy.kopringstudy.member.domain.utli.PasswordUtil
import kopringstudy.kopringstudy.member.domain.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MemberTest {

    @Test
    fun updatePwTest() {
        //given
        val email = "test1@gmail.com"
        val pw = "1234"
        val name = "test1"
        val age = 20
        val member = Member.create(email, pw, name, age)

        //when
        val updatedPw = "1111"
        member.updatePw(updatedPw, pw)

        //then
        Assertions.assertThat(PasswordUtil.isMatchPassword(updatedPw, member.pw))
            .isTrue()
    }

    @Test
    fun updateNameTest() {
        //given
        val email = "test2@gmail.com"
        val pw = "1234"
        val name = "test2"
        val age = 21
        val member = Member.create(email, pw, name, age)

        //when
        val updatedName = "updated_test2"
        member.updateName(updatedName)

        //then
        Assertions.assertThat(member.name)
            .isEqualTo(updatedName)
    }

    @Test
    fun updateAgeTest() {
        //given
        val email = "test3@gmail.com"
        val pw = "1234"
        val name = "test3"
        val age = 22
        val member = Member.create(email, pw, name, age)

        //when
        member.updateAge()

        //then
        Assertions.assertThat(member.age)
            .isEqualTo(23)
    }
}
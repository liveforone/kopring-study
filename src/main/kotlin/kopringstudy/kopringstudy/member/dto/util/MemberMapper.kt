package kopringstudy.kopringstudy.member.dto.util

import kopringstudy.kopringstudy.member.domain.Member
import kopringstudy.kopringstudy.member.dto.response.MemberResponse

object MemberMapper {
    fun entityToDto(member: Member): MemberResponse {
        return MemberResponse(
            member.id,
            member.identity,
            member.name,
            member.age,
            member.auth
        )
    }
}
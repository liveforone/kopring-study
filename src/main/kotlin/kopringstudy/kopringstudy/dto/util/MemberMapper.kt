package kopringstudy.kopringstudy.dto.util

import kopringstudy.kopringstudy.domain.Member
import kopringstudy.kopringstudy.dto.MemberResponse

object MemberMapper {
    fun entityToDto(member:Member): MemberResponse {
        return MemberResponse(
            member.id,
            member.identity,
            member.name,
            member.age,
            member.auth
        )
    }
}
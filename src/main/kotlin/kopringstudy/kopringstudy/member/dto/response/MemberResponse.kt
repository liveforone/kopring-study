package kopringstudy.kopringstudy.member.dto.response

import kopringstudy.kopringstudy.member.domain.Member
import kopringstudy.kopringstudy.member.domain.Role

data class MemberResponse(
    var id: Long?,
    var identity: String,
    var name: String,
    var age: Int,
    var auth: Role
) {
    companion object {
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
}

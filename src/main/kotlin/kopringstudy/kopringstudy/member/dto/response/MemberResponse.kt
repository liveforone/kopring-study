package kopringstudy.kopringstudy.member.dto.response

import kopringstudy.kopringstudy.member.domain.Role

data class MemberResponse(
    var id: Long?,
    var identity: String,
    var name: String,
    var age: Int,
    var auth: Role
)

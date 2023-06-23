package kopringstudy.kopringstudy.dto

import kopringstudy.kopringstudy.domain.Role

data class MemberResponse(
    var id: Long?,
    var identity: String,
    var name: String,
    var age: Int,
    var auth: Role
)

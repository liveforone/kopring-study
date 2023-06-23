package kopringstudy.kopringstudy.dto

import jakarta.validation.constraints.NotNull

data class MemberRequest(
    @field:NotNull(message = "이메일을 입력하세요.")
    var email:String?,
    @field:NotNull(message = "비밀번호를 입력하세요.")
    var pw:String?,
    @field:NotNull(message = "이름을 입력하세요.")
    var name:String?,
    @field:NotNull(message = "나이를 입력하세요.")
    var age:Int?
)

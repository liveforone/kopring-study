package kopringstudy.kopringstudy.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SignupRequest(
    @field:NotBlank(message = "이메일을 입력하세요.")
    var email:String?,
    @field:NotBlank(message = "비밀번호를 입력하세요.")
    var pw:String?,
    @field:NotBlank(message = "이름을 입력하세요.")
    var name:String?,
    @field:NotNull(message = "나이를 입력하세요.")
    var age:Int?
)

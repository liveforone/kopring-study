package kopringstudy.kopringstudy.member.dto.update

import jakarta.validation.constraints.NotBlank

data class ChangePassword(
    @field:NotBlank(message = "새 비밀번호를 입력하세요.")
    var password:String?,
    @field:NotBlank(message = "기존 비밀번호를 입력하세요.")
    var oldPassword:String?
)

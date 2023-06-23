package kopringstudy.kopringstudy.dto

import jakarta.validation.constraints.NotNull

data class ChangePassword(
    @field:NotNull(message = "새 비밀번호를 입력하세요.")
    var password:String?,
    @field:NotNull(message = "기존 비밀번호를 입력하세요.")
    var oldPassword:String?
)

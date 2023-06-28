package kopringstudy.kopringstudy.validator

import kopringstudy.kopringstudy.member.domain.Role
import kopringstudy.kopringstudy.exception.exception.BindingCustomException
import kopringstudy.kopringstudy.exception.exception.MemberCustomException
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import java.util.Objects

@Component
class ControllerValidator {

    fun validateBinding(bindingResult: BindingResult) {
        if (bindingResult.hasErrors()) {
            val errorMessage = Objects.requireNonNull(bindingResult.fieldError)?.defaultMessage
            throw errorMessage?.let { BindingCustomException(it) }!!
        }
    }

    fun validateAdmin(auth:String) {
        if (auth != Role.ADMIN.name) throw MemberCustomException("어드민이 아닙니다.")
    }
}
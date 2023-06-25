package kopringstudy.kopringstudy.exception.controllerAdvice

import kopringstudy.kopringstudy.exception.exception.BindingCustomException
import kopringstudy.kopringstudy.exception.exception.JwtCustomException
import kopringstudy.kopringstudy.exception.exception.MemberCustomException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class MemberControllerAdvice {

    @ExceptionHandler(BadCredentialsException::class)
    fun loginFailHandle():ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("로그인에 실패했습니다.")
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun duplicateEntityValueExceptionHandle(): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("동일한 값이 존재합니다.")
    }

    @ExceptionHandler(BindingCustomException::class)
    fun bindingErrorHandle(customException: BindingCustomException): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(customException.message)
    }

    @ExceptionHandler(MemberCustomException::class)
    fun memberCustomExceptionHandle(memberCustomException: MemberCustomException): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(memberCustomException.message)
    }

    @ExceptionHandler(JwtCustomException::class)
    fun jwtCustomException(jwtCustomException: JwtCustomException): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(jwtCustomException.message)
    }
}
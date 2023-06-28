package kopringstudy.kopringstudy.member.controller.response

import kopringstudy.kopringstudy.member.dto.response.MemberResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object MemberResponse {

    private const val SIGNUP_SUCCESS = "회원가입에 성공하였습니다.\n 반갑습니다."
    private const val LOGIN_SUCCESS = "로그인을 성공하였습니다."
    private const val UPDATE_PASSWORD_SUCCESS = "비밀번호 변경에 성공하였습니다."
    private const val PROHIBITION = "권한이 없습니다."

    fun signupSuccess(): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SIGNUP_SUCCESS)
    }

    fun loginSuccess(): ResponseEntity<*> = ResponseEntity.ok(LOGIN_SUCCESS)

    fun myInfoSuccess(memberResponse: MemberResponse): ResponseEntity<*> = ResponseEntity.ok(memberResponse)

    fun searchMemberSuccess(memberResponse: List<MemberResponse>): ResponseEntity<*> = ResponseEntity.ok(memberResponse)

    fun updatePasswordSuccess(): ResponseEntity<*> {
        return ResponseEntity.ok(UPDATE_PASSWORD_SUCCESS)
    }

    fun adminPageSuccess(allMember: List<MemberResponse>): ResponseEntity<*> = ResponseEntity.ok(allMember)

    fun prohibition(): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(PROHIBITION)
    }
}
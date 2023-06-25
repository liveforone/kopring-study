package kopringstudy.kopringstudy.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import kopringstudy.kopringstudy.authenicationInfo.AuthenticationInfo
import kopringstudy.kopringstudy.controller.constant.MemberUrl
import kopringstudy.kopringstudy.dto.ChangePassword
import kopringstudy.kopringstudy.dto.LoginRequest
import kopringstudy.kopringstudy.dto.MemberRequest
import kopringstudy.kopringstudy.jwt.constant.JwtConstant
import kopringstudy.kopringstudy.service.command.MemberCommandService
import kopringstudy.kopringstudy.service.query.MemberQueryService
import kopringstudy.kopringstudy.validator.ControllerValidator
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

@RestController
class MemberController @Autowired constructor(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService,
    private val authenticationInfo: AuthenticationInfo,
    private val controllerValidator: ControllerValidator
) {

    @PostMapping(MemberUrl.SIGNUP)
    fun singUp(
        @RequestBody @Valid memberRequest: MemberRequest,
        bindingResult: BindingResult
    ) :ResponseEntity<*> {
        controllerValidator.validateBinding(bindingResult)

        memberCommandService.createMember(memberRequest)
        logger().info("회원가입 성공")

        return ResponseEntity.ok("회원가입에 성공했습니다.")
    }

    @PostMapping(MemberUrl.LOGIN)
    fun login(
        @RequestBody @Valid loginRequest: LoginRequest,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ) :ResponseEntity<*> {
        controllerValidator.validateBinding(bindingResult)

        val tokenInfo = memberCommandService.login(loginRequest)
        response.addHeader(JwtConstant.ACCESS_TOKEN, tokenInfo.accessToken)
        response.addHeader(JwtConstant.REFRESH_TOKEN, tokenInfo.refreshToken)

        return ResponseEntity.ok("로그인 성공")
    }

    @GetMapping(MemberUrl.MEMBER_INFO)
    fun myInfo(principal: Principal):ResponseEntity<*> {
        val member = memberQueryService.getMemberByIdentity(principal.name)
        return ResponseEntity.ok(member)
    }

    @GetMapping(MemberUrl.SEARCH_MEMBER)
    fun searchMember(@RequestParam keyword:String): ResponseEntity<*> {
        val member = memberQueryService.searchMember(keyword)
        return ResponseEntity.ok(member)
    }

    @PutMapping(MemberUrl.UPDATE_PASSWORD)
    fun updatePassword(
        @RequestBody @Valid changePassword: ChangePassword,
        bindingResult: BindingResult,
        principal: Principal
    ): ResponseEntity<*> {
        controllerValidator.validateBinding(bindingResult)

        memberCommandService.updatePw(changePassword, principal.name)

        return ResponseEntity.ok("비밀번호 변경에 성공하였습니다.")
    }

    @GetMapping(MemberUrl.ALL_MEMBER_ADMIN_PAGE)
    fun findAllMemberAdminPage(request: HttpServletRequest): ResponseEntity<*> {
        controllerValidator.validateAdmin(authenticationInfo.getAuth(request))

        val allMember = memberQueryService.getAllMemberForAdmin()
        logger().info("!! Admin Access in Admin Page : ${authenticationInfo.getUsername(request)}")

        return ResponseEntity.ok(allMember)
    }

    @GetMapping(MemberUrl.PROHIBITION)
    fun prohibition(): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.")
    }
}
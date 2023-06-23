package kopringstudy.kopringstudy.authenicationInfo

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.http.HttpServletRequest
import kopringstudy.kopringstudy.exception.exception.JwtCustomException
import kopringstudy.kopringstudy.jwt.constant.JwtConstant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.Key
import java.util.*

@Component
class AuthenticationInfo(@Value(JwtConstant.SECRET_KEY_PATH) secretKey: String) {

    private val key: Key

    init {
        val keyBytes: ByteArray = Base64.getDecoder().decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun getUsername(request: HttpServletRequest): String {
        val token = resolveToken(request)
        val claims = getAuthentication(token)
        return claims.subject
    }

    fun getAuth(request: HttpServletRequest): String {
        val token = resolveToken(request)
        val claims = getAuthentication(token)
        return claims[JwtConstant.CLAIM_NAME].toString()
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(JwtConstant.HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConstant.BEARER_TOKEN)) {
            bearerToken.substring(JwtConstant.TOKEN_SUB_INDEX)
        } else{
            throw JwtCustomException(JwtConstant.EMPTY_CLAIMS)
        }
    }

    private fun getAuthentication(accessToken: String?): Claims {
        return parseClaims(accessToken)
    }

    private fun parseClaims(accessToken: String?): Claims {
        accessToken ?: throw JwtCustomException(JwtConstant.TOKEN_IS_NULL)

        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: MalformedJwtException) {
            throw JwtCustomException(JwtConstant.INVALID_MESSAGE)
        } catch (e: UnsupportedJwtException) {
            throw JwtCustomException(JwtConstant.UNSUPPORTED_MESSAGE)
        } catch (e: SecurityException) {
            throw JwtCustomException(JwtConstant.INVALID_MESSAGE)
        } catch (e: ExpiredJwtException) {
            throw JwtCustomException(JwtConstant.EXPIRED_MESSAGE)
        }
    }
}
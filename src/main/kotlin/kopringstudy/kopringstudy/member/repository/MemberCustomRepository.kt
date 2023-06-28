package kopringstudy.kopringstudy.member.repository

import kopringstudy.kopringstudy.member.domain.Member
import kopringstudy.kopringstudy.member.dto.response.MemberResponse

interface MemberCustomRepository {
    fun findOneByEmail(email: String): Member
    fun findOneByIdentity(identity: String): Member
    fun findAllMember(): List<MemberResponse>
    fun searchMember(keyword: String): List<MemberResponse>
}
package kopringstudy.kopringstudy.repository

import kopringstudy.kopringstudy.domain.Member
import kopringstudy.kopringstudy.dto.MemberResponse

interface MemberCustomRepository {
    fun findOneByEmail(email: String) :Member
    fun findOneByIdentity(identity:String):Member
    fun findAllMember(): List<MemberResponse>
    fun searchMember(keyword:String): List<MemberResponse>
}
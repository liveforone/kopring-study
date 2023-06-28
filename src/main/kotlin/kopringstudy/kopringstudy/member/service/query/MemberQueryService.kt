package kopringstudy.kopringstudy.member.service.query

import kopringstudy.kopringstudy.member.dto.response.MemberResponse
import kopringstudy.kopringstudy.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService @Autowired constructor(
    private val memberRepository: MemberRepository
) {

    fun getMemberByIdentity(identity:String): MemberResponse {
        return MemberResponse.entityToDto(memberRepository.findOneByIdentity(identity))
    }

    fun getMemberByEmail(email: String): MemberResponse {
        return MemberResponse.entityToDto(memberRepository.findOneByEmail(email))
    }

    fun searchMember(keyword:String): List<MemberResponse> = memberRepository.searchMember(keyword)

    fun getAllMemberForAdmin(): List<MemberResponse> = memberRepository.findAllMember()
}
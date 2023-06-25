package kopringstudy.kopringstudy.service.query

import kopringstudy.kopringstudy.dto.MemberResponse
import kopringstudy.kopringstudy.dto.util.MemberMapper
import kopringstudy.kopringstudy.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService @Autowired constructor(
    private val memberRepository:MemberRepository
) {

    fun getMemberByIdentity(identity:String): MemberResponse {
        return MemberMapper.entityToDto(memberRepository.findOneByIdentity(identity))
    }

    fun getMemberByEmail(email: String): MemberResponse {
        return MemberMapper.entityToDto(memberRepository.findOneByEmail(email))
    }

    fun searchMember(keyword:String): List<MemberResponse> {
        return memberRepository.searchMember(keyword)
    }

    fun getAllMemberForAdmin(): List<MemberResponse> {
        return memberRepository.findAllMember()
    }
}
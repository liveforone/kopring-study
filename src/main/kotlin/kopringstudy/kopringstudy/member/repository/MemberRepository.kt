package kopringstudy.kopringstudy.member.repository

import kopringstudy.kopringstudy.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {
}
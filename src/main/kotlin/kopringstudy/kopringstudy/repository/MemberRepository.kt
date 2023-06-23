package kopringstudy.kopringstudy.repository

import kopringstudy.kopringstudy.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {
}
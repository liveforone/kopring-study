package kopringstudy.kopringstudy.repository

import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import jakarta.persistence.NoResultException
import kopringstudy.kopringstudy.domain.Member
import kopringstudy.kopringstudy.dto.MemberResponse
import kopringstudy.kopringstudy.exception.constant.ExceptionMessage
import kopringstudy.kopringstudy.exception.exception.MemberCustomException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl @Autowired constructor(private val queryFactory: SpringDataQueryFactory): MemberCustomRepository {

    override fun findOneByEmail(email: String): Member {
        return try {
             queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(column(Member::email).equal(email))
            }
        } catch (e: NoResultException) {
            throw MemberCustomException(ExceptionMessage.MEMBER_IS_NULL.message)
        }
    }

    override fun findOneByIdentity(identity: String): Member {
        return try {
            queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(column(Member::identity).equal(identity))
            }
        } catch (e: NoResultException) {
            throw MemberCustomException(ExceptionMessage.MEMBER_IS_NULL.message)
        }
    }

    override fun findAllMember(): List<MemberResponse> {
        return queryFactory.listQuery {
            select(listOf(
                column(Member::id),
                column(Member::identity),
                column(Member::name),
                column(Member::age),
                column(Member::auth)
            ))
            from(entity(Member::class))
            orderBy(column(Member::id).desc())
        }
    }

    override fun searchMember(keyword: String): List<MemberResponse> {
        return queryFactory.listQuery {
            select(listOf(
                column(Member::id),
                column(Member::identity),
                column(Member::name),
                column(Member::age),
                column(Member::auth)
            ))
            from(entity(Member::class))
            where(column(Member::email).like("${keyword}%"))
            orderBy(column(Member::id).desc())
        }
    }
}
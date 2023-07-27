# 코프링(코틀린 + 스프링) 예제
* 직접 공부하며 실습한 예제입니다.
* 제가 보기위해 작성했습니다 ^^

# 1. 시큐리티
## jwt 예외 핸들링
* 마이크로서비스로 구현하게되면 회원 서비스를 분리가능하다.
* 이렇게 되면 회원서비스에서 jwt 안에 토큰정보를 파싱할때 부적절한 토큰으로 인해 에러가 발생하면 로그만 찍어주고
* 다른 서비스에서는 예외에 대한 핸들링을 처리해주면된다.
* 회원서비스에서는 예외에 대한 핸들링을 처리하려면 필터나 인터셉터로 넘어오는 예외는 일반적인 예외 핸들러가 처리하지 못하므로
* 엔트리 포인트나 이것저것 건들여주어야한다.
## 시큐리티 설정
* 시큐리티에서 설정은 설정Configurer<HttpSecurity> 형태로 정의해야한다.
* 또한 람다식을 사용한다.
## 로그인 위임
* 시큐리티에게 로그인을 위임하는 코드는 다음과 같다.
```kotlin
authenticationManagerBuilder.also {
            it.`object`.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.pw)
            )
}
```

# 2. 달라진 점
## @Value 어노테이션
* 코틀린에서는 문자열 템프릿에 ${}를 사용하므로 @Value 어노테이션을 사용할때 상수를 이용하려면 아래와 같이 해야한다.
```
const val SECRET_KEY_PATH = "\${jwt.secret}"
```
## 로그
* lombok을 사용하면 @Slf4j를 사용해 편하게 로그를 찍을 수 있으나, 코틀린에서는 롬북을 사용하지 않으므로
* 아래처럼 인라인 함수를 프로젝트 상단에 작성하여 사용하면된다.
```kotlin
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

class Xx { }
```
## 와일드 카드
* ResponseEntity같이 객체를 담는 제네릭에는 ?를 이용해 와일드 카드를 작성해왔었다.
* 이는 코틀린에서 *로 사용한다.
```
ResponseEntity<*>
```
## 생성자 주입
* 롬북이 없어서 @RequiredArgsConstructor 어노테이션을 사용하지 못하므로 아래와 같이 생성자 주입한다.
```
@Autowired constructor(private val ..)
```
## object
* 상수와 static 메서드를 사용해야하는 클래스는 object로 선언한다.
## validation
* validation 시에는 @NotNull 과 같이 어노테이션을 사용하는게 아니라
* @field:NotNull 로 사용한다.
* 또한 입력받는 값이 없을 수도 있으므로 반드시 dto의 필드는 ?, 즉 nullable하게 선언한다.
* 다른 클래스에서 해당 dto를 사용시 !!를 이용해서 값이 반드시 있다고 선언해주어라.
* 이유는 이미 validation을 통해서 값이 있다고 검증이 끝났기 때문이다.
```
data class ChangePassword(
    @field:NotBlank(message = "새 비밀번호를 입력하세요.")
    var password:String?,
    @field:NotBlank(message = "기존 비밀번호를 입력하세요.")
    var oldPassword:String?
)

fun updatePw(changePassword: ChangePassword, identity:String) {
        val member = memberRepository.findOneByIdentity(identity)
        member.updatePw(changePassword.password!!, changePassword.oldPassword!!)
}
```
## Int 타입 @NotNull 밸리데이션 가능
* Java에서는 원시 자료형에 null 값을 할당할 수 없고, 원시 자료형에 대한 값은 기본적으로 0 또는 해당 자료형의 기본 값으로 초기화된다.
* 코틀린은 기본적으로 모든 변수는 null이 될 수 있는 것으로 취급된다.
* 따라서 코틀린은 원시자료형도 Null이 될 수 있다. 이에 따라 코틀린에서는 Int형에도 @NotNull 어노테이션 사용이 가능하다.
## 코틀린 사용시 Embeddable
* 기본생성자가 필요해 noargs에 추가
* final이면 안되기 때문에 allopen에 추가
* 프라퍼티에는 getter/setter 존재해야함. -> var로 선언
## 코틀린 사용시 idclass
* noargs가 필요하므로, 필드에 "" 혹은 null로 값 추가.
* 또한 컬럼명을 지정해주자. ImplicitNamingStrategy를 사용할 수 있게된다.

# 3. 좋은 습관, 코틀린 스러운
## 클라이언트에게 리턴하지 않을 컬럼만 private
* 비밀번호와 같이 클라이언트에게 리턴하지 않을 값만 private으로 선언한다.
## 식을 사용할때
* if, when, try-catch 등은 모두 식이다.
* 이들을 사용할떄에는 보다 간편하게 아래와 같이 리턴하자.
```
return try {
            todo
      } catch() {
            todo
      }
```
## 문자열 템플릿
* '$문자열' 로 사용하는 문자열 템플릿은 한 문자 밖에 인식하지 못하므로 꼭 '${문자열}' 로 쓰는 습관을 들이자.
```
logger().info("!! Admin Access in Admin Page : ${authenticationInfo.getUsername(request)}")
```
## 서비스 계층 객체 save
* 서비스 계층에서 엔티티를 insert 할때, 아래처럼 코틀린스러우며 우아한 코드가 작성가능하다.
* val member = Member.create(..) memberRepository.save(member) 보다 훨씬 우아하다.
```
fun createMember(requestDto:MemberRequest) {
        Member.create(requestDto.email!!, requestDto.pw!!, requestDto.name!!, requestDto.age!!)
            .also {
                memberRepository.save(it)
            }
}
```
## 스코프 함수
* 스코프 함수는 **속성을 사용거나** **속성을 읽는 경우**에 사용해야한다.
* 내부 상태를 변경하는 경우는 스코프 함수보다는 일반적인 할당을 사용하는 것이 좋다.
```kotlin
//[bad]
val person = Person("Alice", 25)
person.apply {
        println("현재 나이: $age")
        age = 30 // apply 함수 내에서 상태 변경
        println("변경된 나이: $age")
}

//[good]
val person = Person("Ted", 20)
person.age = 30
person.apply {
    println("변경된 나이: $age")
}
```
## 이해하기 어려운 매개변수의 경우 이름지정 파라미터를 사용하라
* 프레임워크나 라이브러리를 통해 전달되는 값의 경우 의미를 이해하기 어려운 경우가 많다.
* 이때에는 이름지정 파라미터를 사용하여 의미를 명시하면 좋다.
* 단 코틀린이 아닌 자바 함수들은 이름지정 파라미터 사용이 불가능하다.
## 임베디드 컬럼
* 임베디드는 반드시 getter와 setter가 존재해야한다. 따라서 var로 선언한다.
## 복합키
* 복합키는 컬럼명을 지정해준다.
* pk클래스의 경우 기본생성자가 필요하기에 기본값을 지정한다.(null, string은 "")

# 4. jdsl
## jdsl 사용
* query dsl 을 지금까지 편하게 써왔으나, 코틀린에서는 설정해야할 것들이 너무 많고 안정적이지않아,
* 라인에서 개발한 jdsl을 사용한다.
* qclass도 필요없고, 중강중간 qclass로 문제를 발생시키지 않는다. 
* 개발도중 변경시 지속적으로 빌드를 해주지 않아도 아무 문제가 없다.
* jdsl은 criterial를 기반으로 작동하는 점을 이용해, 원하는 쿼리가 있으면 criteria를 잘 살펴보고 이용하면된다.(jdsl 래퍼런스가 내용이 부실하다.)
* 필요한 종속성은 아래와 같다.
```
implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter-jakarta:버전")
```
## 컬럼
* colum()과 col() 모두 사용가능하다.
## 단건 쿼리 처리시 jdsl 주의점
* criteria 기반의 쿼리 라이브러리를 쓸때 주의 할점은 null 이다.
* entity? 를 리턴하는 것이 불가능하고, 이 경우 null이면 NoResultException 을 터뜨리게된다.
* 따라서 try-catch 문으로 잡아주는것이 좋다.
* 컬렉션조회의 경우 빈 컬렉션이 나가기때문에 상관이 없지만,
* 단건 조회의 경우 꼭 try-catch 로 exception 을 처리해야한다.
```kotlin
override fun findOneByEmail(email: String): Member {
        return try {
             queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(column(Member::email).equal(email))
            }
        } catch (e: NoResultException) {
            throw MemberCustomException("존재하지 않는 회원입니다.")
        }
}
```
## dto projection
* dto projection을 지원하며 아래와 같이 사용한다.
```
select(listOf(
        column(Member::id),
        column(Member::identity),
        column(Member::name),
        column(Member::age),
        column(Member::auth)
))
```
## 검색
* like 쿼리는 인덱스를 타는 keyword% 쿼리를 사용하는 것이 좋다.
* 어떤 쿼리던 like쿼리는 직접 %문자를 붙어주어야한다.
```
where(column(Member::email).like("${keyword}%"))
where(col(Member::email).like("${%keyword}%"))
```
## 동적쿼리 및 페이징
* 페이징은 쿼리 dsl을 사용할때 쓰던 방식과 유사하다.
* 여기서 말하는 페이징은 no-offset 페이징 방식을 의미한다.
* no-offset 방식을 사용하려면 동적 쿼리가 필요하다.
* 아래는 no-offset 페이징 쿼리이다.(자바코드)
```
public List<Item> findItemsByShopId(Long shopId, Long lastId, int pageSize) {
        return queryFactory.selectFrom(item)
                .where(
                        item.shopId.eq(shopId),
                        ltItemId(lastId)
                )
                .orderBy(
                        item.good.desc(),
                        item.id.desc()
                )
                .limit(pageSize)
                .fetch();
}
```
* ltItemId는 입력받은 lastId보다 작은 값들(desc기준, asc는 큰 값들)을 가져오라는 조건을 의미한다.
* 그러나 첫번째 페이지의 경우 lastId가 null일 수도 있다.
* 이 경우에는 desc 기준 가장 최신 id를 알 수 없으므로, 해당 쿼리 코드를 무시하도록 해 다른 조건만 걸 수 있도록 한다.
* 아래는 코틀린으로 구현된 jdsl 동적쿼리이다.
* title이 null이면 무시하고, null이 아니면 동작한다. authorName도 마찬가지이다.
```kotlin
where(dynamicAndFactory(title, authorName))

private fun <T> SpringDataCriteriaQueryDsl<T>.dynamicAndFactory(
	title: String?,
	authorName: String?
): PredicateSpec {
    return and(
        title?.let { column(PostEntity::title).equal(title) },
        authorName?.let { column(AuthorEntity::name).equal(authorName)}
    )
}
```
## 시간비교
* 시간 비교는 생각보다 많이 발생한다.
* 아래와 같이 작성가능하다.
```
column(Book::createdAt).between(Time.of("2001-01-01"), Time.of("2010-12-31"))
```
## 쿼리 조건
* 자주 사용하는 쿼리 조건은 아래와 같다.
* lessThan
* lessThanOrEqualTo
* greaterThan
* greaterThanOrEqualTo 

# 번외 
## 트랜잭션 중첩
1. 첫 번째 메서드의 트랜잭션 시작 (예: @Transactional 시작)
2. 첫 번째 메서드 내에서 두 번째 메서드 호출
3. 두 번째 메서드의 트랜잭션 시작 (예: @Transactional 시작)
4. 두 번째 메서드 수행 (두 번째 메서드 내에서 쿼리 실행)
5. 두 번째 메서드의 트랜잭션 종료 (예: @Transactional 종료, 커밋 또는 롤백)
6. 첫 번째 메서드 수행 (첫 번째 메서드 내에서 쿼리 실행)
7. 첫 번째 메서드의 트랜잭션 종료 (예: @Transactional 종료, 커밋 또는 롤백)
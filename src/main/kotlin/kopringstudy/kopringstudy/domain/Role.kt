package kopringstudy.kopringstudy.domain

enum class Role(val auth:String) {
    MEMBER("ROLE_MEMBER"), ADMIN("ROLE_ADMIN");
}
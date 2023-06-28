package kopringstudy.kopringstudy.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kopringstudy.kopringstudy.member.domain.Role


@Converter
class RoleConverter : AttributeConverter<Role, String> {
    override fun convertToDatabaseColumn(attribute: Role): String = attribute.name

    override fun convertToEntityAttribute(dbData: String): Role = Role.valueOf(dbData)
}
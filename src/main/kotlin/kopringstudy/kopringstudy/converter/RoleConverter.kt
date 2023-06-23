package kopringstudy.kopringstudy.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kopringstudy.kopringstudy.domain.Role


@Converter
class RoleConverter : AttributeConverter<Role, String> {
    override fun convertToDatabaseColumn(attribute: Role): String {
        return attribute.name
    }

    override fun convertToEntityAttribute(dbData: String): Role {
        return Role.valueOf(dbData)
    }
}
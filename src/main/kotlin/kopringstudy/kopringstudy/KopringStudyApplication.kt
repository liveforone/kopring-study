package kopringstudy.kopringstudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.HiddenHttpMethodFilter

@SpringBootApplication
class KopringStudyApplication

fun main(args: Array<String>) {
	runApplication<KopringStudyApplication>(*args)

	@Bean
	fun hiddenMethodFilter(): HiddenHttpMethodFilter = HiddenHttpMethodFilter()
}

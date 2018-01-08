package cn.com.guardiantech.aofgo.backend.jackson

import cn.com.guardiantech.aofgo.backend.jackson.deserializer.DateDeserializer
import cn.com.guardiantech.aofgo.backend.jackson.serializer.DateSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.util.*


/**
 * Created by dedztbh on 1/7/18.
 * Project AOFGoBackend
 */
@Configuration
class JacksonConfiguration: WebMvcConfigurerAdapter() {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>?) {
        val converter = MappingJackson2HttpMessageConverter()
        val objectMapper = this.objectMapper()
        converter.objectMapper = objectMapper
        converters?.add(converter)
        super.configureMessageConverters(converters)
    }

    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()

        module.addDeserializer(Date::class.java, DateDeserializer())
        module.addSerializer(Date::class.java, DateSerializer())

        mapper.registerModule(module)
        return mapper
    }
}
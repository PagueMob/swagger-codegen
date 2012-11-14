package com.wordnik.swagger.codegen.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{DeserializationFeature, SerializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JsonUtil {
  def mapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(new DefaultScalaModule())
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    mapper
  }
}

object XmlUtil {
  //def mapper
}
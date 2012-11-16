package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.generator._
import com.wordnik.swagger.codegen.model._
import com.wordnik.swagger.codegen.util.SwaggerType

object IOSGenerator extends IOSGenerator

class IOSGenerator extends BasicGenerator with BasicGeneratorMapper with BasicGeneratorWriter with BasicGeneratorConfig{
  override protected def templateDir = "IOS"
  override protected def destinationDir = super.destinationDir + "/IOS"

  protected def apiTemplateFiles = List(
    TemplateFile("api-header.mustache", ".h", Some("api")),
    TemplateFile("api-body.mustache", ".m", Some("api"))
  )

  protected def modelTemplateFiles = List(
    TemplateFile("model-header.mustache", ".h", Some("model")),
    TemplateFile("model-body.mustache", ".m", Some("model"))
  )

  override protected def typeMapping = List(
    TypeMapper(SwaggerType.Byte, "char", isPrimitive = true),
    TypeMapper(SwaggerType.Boolean, "BOOL", isPrimitive = true),
    TypeMapper(SwaggerType.Int, "int", isPrimitive = true),
    TypeMapper(SwaggerType.Long, "long", isPrimitive = true),
    TypeMapper(SwaggerType.Float, "float", isPrimitive = true),
    TypeMapper(SwaggerType.Double, "double", isPrimitive = true),
    TypeMapper(SwaggerType.String, "NSString"),
    TypeMapper(SwaggerType.Date, "NSDate"),
    TypeMapper(SwaggerType.List, "NSArray"),
    TypeMapper(SwaggerType.Array, "NSArray"),
    TypeMapper(SwaggerType.Set, "NSArray"),
    TypeMapper(SwaggerType.Void, "void")
  )
  override protected def mapHttpMethod(name: String): String = {
    name.toUpperCase
  }

  override protected def mapGetterName(name: String, dataType: String) = {
    val base = dataType match {
      case SwaggerType.Boolean => "is"
      case _ => "get"
    }
    base + name(0).toUpper + name.substring(1)
  }

  override protected def mapSetterName(name: String, dataType: String) = {
    val base = "set"
    base + name(0).toUpper + name.substring(1)
  }
}
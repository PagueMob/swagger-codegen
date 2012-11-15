package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.generator._
import com.wordnik.swagger.codegen.model._
import com.wordnik.swagger.codegen.util.SwaggerType

object iOSGenerator extends iOSGenerator

class iOSGenerator extends BasicGenerator with BasicGeneratorMapper with BasicGeneratorWriter with BasicGeneratorConfig{
  override protected def templateDir = "iOS"
  override protected def destinationDir = super.destinationDir + "/iOS"

  protected def apiTemplateFiles = List(
    TemplateFile("api-header.mustache", ".h", Some("api")),
    TemplateFile("api-body.mustache", ".m", Some("api"))
  )

  protected def modelTemplateFiles = List(
    TemplateFile("model-header.mustache", ".h", Some("model")),
    TemplateFile("model-body.mustache", ".m", Some("model"))
  )

  override protected def typeMapping = List(
    TypeMapper(SwaggerType.Byte, "char"),
    TypeMapper(SwaggerType.Boolean, "BOOL"),
    TypeMapper(SwaggerType.Int, "int"),
    TypeMapper(SwaggerType.Long, "long"),
    TypeMapper(SwaggerType.Float, "float"),
    TypeMapper(SwaggerType.Double, "double"),
    TypeMapper(SwaggerType.String, "NSString"),
    TypeMapper(SwaggerType.Date, "NSDate"),
    TypeMapper(SwaggerType.List, "NSArray"),
    TypeMapper(SwaggerType.Array, "NSArray"),
    TypeMapper(SwaggerType.Set, "NSArray"),
    TypeMapper(SwaggerType.Void, "void")
  )

  lazy val primitives = List(
    SwaggerType.Byte,
    SwaggerType.Boolean,
    SwaggerType.Int,
    SwaggerType.Long,
    SwaggerType.Float,
    SwaggerType.Double,
    SwaggerType.Void
  )

  override protected def mapTypeName(name: String) = {
    typeMapping.find(_.name == name) match {
      case Some(t) =>
        if (primitives.contains(name)) t.nameTo
        else t.nameTo + "*"
      case None => name + "*"
    }
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
package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.generator.{BasicGenerator => BGenerator, BasicGeneratorWriter, BasicGeneratorConfig}
import com.wordnik.swagger.codegen.model.{TypeMapper, TemplateFile}
import com.wordnik.swagger.codegen.util.SwaggerType

object JavaGenerator extends JavaGenerator

class JavaGenerator extends BGenerator with BasicGeneratorConfig with BasicGeneratorWriter {
  protected def templateDir = "new_java"

  protected def apiTemplateFiles = List(TemplateFile("api.mustache", ".java"))

  protected def modelTemplateFiles = List(TemplateFile("model.mustache", ".java"))

  override protected def sourceDir: String = "java"

  override protected def apiPackage: String = "com.swagger.api"

  override protected def modelPackage: String = "com.swagger.model"

  override protected def typeMapping = List(
    TypeMapper(SwaggerType.Byte, "byte"),
    TypeMapper(SwaggerType.Boolean, "boolean"),
    TypeMapper(SwaggerType.Int, "int"),
    TypeMapper(SwaggerType.Long, "long"),
    TypeMapper(SwaggerType.Float, "float"),
    TypeMapper(SwaggerType.Double, "double"),
    TypeMapper(SwaggerType.String, "String"),
    TypeMapper(SwaggerType.Date, "Date", Some("java.util")),
    TypeMapper(SwaggerType.List, "List", Some("java.util")),
    TypeMapper(SwaggerType.Set, "Set", Some("java.util")),
    TypeMapper(SwaggerType.Array, "List", Some("java.util")),
    TypeMapper(SwaggerType.Map, "Map", Some("java.util")),
    TypeMapper(SwaggerType.Object, "Object"),
    TypeMapper(SwaggerType.Void, "void")
  )

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
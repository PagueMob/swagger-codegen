package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.generator._
import com.wordnik.swagger.codegen.model._
import com.wordnik.swagger.codegen.util.SwaggerType
import java.io.File

object JavaGenerator extends JavaGenerator

class JavaGenerator extends BasicGenerator with BasicGeneratorMapper with BasicGeneratorWriter with BasicGeneratorConfig {
  override protected def templateDir = "Java"
  override protected def destinationDir = super.destinationDir + "/java"
  override protected def apiPackage = Some("com.swagger.api")
  override protected def modelPackage = Some("com.swagger.model")

  protected def apiTemplateFiles = List(
    TemplateFile("api.mustache", ".java", apiPackage.map(_.replace(packageSeparator, File.separator)))
  )

  protected def modelTemplateFiles = List(
    TemplateFile("model.mustache", ".java", modelPackage.map(_.replace(packageSeparator, File.separator)))
  )

  override protected def typeMapping = List(
    TypeMapper(SwaggerType.Byte, "byte", isPrimitive = true),
    TypeMapper(SwaggerType.Boolean, "boolean", isPrimitive = true),
    TypeMapper(SwaggerType.Int, "int", isPrimitive = true),
    TypeMapper(SwaggerType.Long, "long", isPrimitive = true),
    TypeMapper(SwaggerType.Float, "float", isPrimitive = true),
    TypeMapper(SwaggerType.Double, "double", isPrimitive = true),
    TypeMapper(SwaggerType.String, "String"),
    TypeMapper(SwaggerType.Date, "Date", Some("java.util")),
    TypeMapper(SwaggerType.List, "List", Some("java.util")),
    TypeMapper(SwaggerType.Array, "List", Some("java.util")),
    TypeMapper(SwaggerType.Set, "Set", Some("java.util")),
    TypeMapper(SwaggerType.Void, "void")
  )

  override protected def mapType(name: String, typeRef: Option[String] = None): TypeInfo = {
    val t = super.mapType(name, typeRef)
    t.name match {
      case "List" => t.copy(name = List(Some("List<"), t.dataRef.map(_.name), Some(">")).flatten.mkString)
      case "Set" => t.copy(name = List(Some("Set<"), t.dataRef.map(_.name), Some(">")).flatten.mkString)
      case _ => t
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
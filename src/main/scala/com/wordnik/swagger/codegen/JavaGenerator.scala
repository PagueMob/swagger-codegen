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
    TypeMapper(SwaggerType.Byte, "byte"),
    TypeMapper(SwaggerType.Boolean, "boolean"),
    TypeMapper(SwaggerType.Int, "int"),
    TypeMapper(SwaggerType.Long, "long"),
    TypeMapper(SwaggerType.Float, "float"),
    TypeMapper(SwaggerType.Double, "double"),
    TypeMapper(SwaggerType.String, "String"),
    TypeMapper(SwaggerType.Date, "Date", Some("java.util")),
    TypeMapper(SwaggerType.List, "List", Some("java.util")),
    TypeMapper(SwaggerType.Array, "List", Some("java.util")),
    TypeMapper(SwaggerType.Set, "Set", Some("java.util")),
    TypeMapper(SwaggerType.Void, "void")
  )

  override protected def mapType(name: String, typeRef: Option[String] = None): TypeInfo = {
    val ref = typeRef.map(t => mapType(t))
    val n = mapTypeName(name) match {
      case "List" => List(Some("List<"), ref.map(_.name), Some(">")).flatten.mkString
      case "Set" => List(Some("Set<"), ref.map(_.name), Some(">")).flatten.mkString
      case t => t
    }
    TypeInfo(n,
      mapPackageName(name),
      mapImport(name),
      ref)
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
package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.model.TypeInfo
import com.wordnik.swagger.codegen.util.SwaggerType

trait GeneratorMapper {
  this: GeneratorConfig =>

  protected def mapModelName(name: String): String
  protected def mapApiName(name: String): String

  protected def mapType(name: String, typeRef: Option[String] = None): TypeInfo
  protected def mapTypeName(name: String): String
  protected def mapPackageName(`type`: String): Option[String]
  protected def mapImport(`type`: String): Option[String]

  protected def mapIsPrimitive(`type`: String): Boolean
  protected def mapIsComplex(`type`: String): Boolean
  protected def mapIsContainer(`type`: String): Boolean


  protected def mapDefaultValue(`type`: String): Option[String]
  protected def mapPropertyName(name: String, dataType: String): String
  protected def mapGetterName(name: String, dataType: String): String
  protected def mapSetterName(name: String, dataType: String): String

  protected def mapMethodName(name: String): String
  protected def mapReturnName(`type`:String):String
  protected def mapHttpMethod(name: String): String
}

trait BasicGeneratorMapper extends GeneratorMapper {
  this: GeneratorConfig =>
  private lazy val ListTypeRegex = """List\[(.*)\]""".r

  override protected def mapModelName(name: String) = name.charAt(0).toUpper + name.substring(1)
  override protected def mapApiName(name: String) = {
    val n = "[^a-zA-Z]".r.replaceAllIn(name, "")
    n.charAt(0).toUpper + n.substring(1) + "Api"
  }

  override protected def mapType(name: String, typeRef: Option[String] = None): TypeInfo = {
    name match {
      case ListTypeRegex(ref) => mapType(SwaggerType.List, Some(ref))
      case _ => TypeInfo(
        mapTypeName(name),
        mapPackageName(name),
        mapImport(name),
        mapIsPrimitive(name),
        mapIsComplex(name),
        mapIsContainer(name),
        typeRef.map(t => mapType(t))
      )
    }
  }

  override protected def mapTypeName(name: String): String = {
    typeMapping.find(_.name == name).map(_.nameTo).getOrElse(name)
  }

  override protected def mapIsPrimitive(`type`: String): Boolean = {
    typeMapping.find(_.name == `type`).map(_.isPrimitive).getOrElse(false)
  }

  override protected def mapIsComplex(`type`: String): Boolean = {
    typeMapping.find(_.name == `type`).map(_.isComplex).getOrElse(true)
  }

  override protected def mapIsContainer(`type`: String): Boolean = {
    typeMapping.find(_.name == `type`).map(_.isContainer).getOrElse(false)
  }

  override protected def mapPackageName(`type`: String): Option[String] = {
    typeMapping.find(_.name == `type`).map(_.`package`).getOrElse(None)
  }

  override protected def mapImport(`type`: String): Option[String] = {
    val r = typeMapping.find(_.name == `type`) match {
      case Some(t) => t.`package` match {
        case Some(p) => Some(List(p, t.nameTo).mkString(packageSeparator))
        case None => None
      }
      case None => Some(List(modelPackage, Some(`type`)).flatten.mkString(packageSeparator))
    }
    r
  }

  override protected def mapDefaultValue(`type`: String): Option[String] = {
    typeMapping.find(_.name == `type`).map(_.defaultValue).getOrElse(None)
  }

  override protected def mapPropertyName(name: String, dataType: String) = name.charAt(0).toLower + name.substring(1)
  override protected def mapMethodName(name: String) = name.charAt(0).toLower + name.substring(1)
  override protected def mapReturnName(name: String) = name.charAt(0).toLower + name.substring(1)
  override protected def mapHttpMethod(name: String) = name.toLowerCase
}

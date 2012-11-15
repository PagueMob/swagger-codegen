package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.model.TypeInfo

trait GeneratorMapper {
  this: GeneratorConfig =>

  protected def mapModelName(name: String): String
  protected def mapApiName(name: String): String

  protected def mapType(name: String, typeRef: Option[String] = None): TypeInfo
  protected def mapTypeName(name: String): String
  protected def mapPackageName(name: String): Option[String]
  protected def mapImport(`type`: String): Option[String]

  protected def mapDefaultValue(`type`: String): Option[String]
  protected def mapPropertyName(name: String, dataType: String): String
  protected def mapGetterName(name: String, dataType: String): String
  protected def mapSetterName(name: String, dataType: String): String

  protected def mapMethodName(name: String): String
  protected def mapHttpMethod(name: String): String
}

trait BasicGeneratorMapper extends GeneratorMapper {
  this: GeneratorConfig =>

  override protected def mapModelName(name: String) = name.charAt(0).toUpper + name.substring(1)
  override protected def mapApiName(name: String) = {
    val n = "[^a-zA-Z]".r.replaceAllIn(name, "")
    n.charAt(0).toUpper + n.substring(1) + "Api"
  }

  override protected def mapType(name: String, typeRef: Option[String] = None): TypeInfo = {
    TypeInfo(mapTypeName(name),
      mapPackageName(name),
      mapImport(name),
      typeRef.map(t => mapType(t)))
  }

  override protected def mapTypeName(name: String) = {
    typeMapping.find(_.name == name) match {
      case Some(t) => t.nameTo
      case None => name
    }
  }

  override protected def mapPackageName(`type`: String) = {
    typeMapping.find(_.name == `type`).map(_.`package`).getOrElse(None)
  }

  override protected def mapImport(`type`: String) = {
    typeMapping.find(_.name == `type`) match {
      case Some(t) => t.`package` match {
          case Some(p) => Some(List(p, `type`).mkString(packageSeparator))
          case None => None
      }
      case None => Some(List(modelPackage, Some(`type`)).flatten.mkString(packageSeparator))
    }
  }

  override protected def mapDefaultValue(`type`: String) = {
    typeMapping.find(_.name == `type`) match {
      case Some(t) => t.defaultValue
      case None => None
    }
  }

  override protected def mapPropertyName(name: String, dataType: String) = name.charAt(0).toLower + name.substring(1)
  override protected def mapMethodName(name: String) = name.charAt(0).toLower + name.substring(1)
  override protected def mapHttpMethod(name: String) = name.toLowerCase
}

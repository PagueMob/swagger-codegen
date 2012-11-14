package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.model.{TypeMapper, TemplateFile}

trait GeneratorConfig {
  protected def generatorDir: String
  protected def sourceDir: String
  protected def templateDir: String

  protected def apiPackage: String
  protected def apiTemplateFiles: List[TemplateFile]

  protected def modelPackage: String
  protected def modelTemplateFiles: List[TemplateFile]

  protected def typeMapping: List[TypeMapper]

  protected def packageSeparator: String

  protected def mapModelName(name: String): String
  protected def mapApiName(name: String): String

  protected def mapTypeName(name: String): String
  protected def mapImport(`type`: String): Option[String]

  protected def mapDefaultValue(`type`: String): Option[String]
  protected def mapPropertyName(name: String, dataType: String): String
  protected def mapGetterName(name: String, dataType: String): String
  protected def mapSetterName(name: String, dataType: String): String

  protected def mapMethodName(name: String): String
}

trait BasicGeneratorConfig extends GeneratorConfig {
  protected def packageSeparator: String = "."
  protected def generatorDir: String = "generated-code"
  protected def sourceDir: String = "src"

  protected def modelPackage: String = "model"
  protected def apiPackage: String = "api"

  protected def mapModelName(name: String) = name.charAt(0).toUpper + name.substring(1)
  protected def mapApiName(name: String) = name.charAt(0).toUpper + name.substring(1) + "Api"

  protected def mapTypeName(name: String) = {
    typeMapping.find(_.name == name) match {
      case Some(t) => t.nameTo
      case None => name
    }
  }

  protected def mapImport(`type`: String) = {
    val t = typeMapping.find(_.name == `type`)
    val p = t.map(_.`package`).getOrElse(Some(modelPackage))
    val r = p.map(p => Some(p + packageSeparator + mapTypeName(`type`))).getOrElse(None)
    r
  }

  protected def mapDefaultValue(`type`: String) = {
    typeMapping.find(_.name == `type`) match {
      case Some(t) => t.defaultValue
      case None => None
    }
  }

  protected def mapPropertyName(name: String, dataType: String) = name.charAt(0).toLower + name.substring(1)
  protected def mapMethodName(name: String) = name.charAt(0).toLower + name.substring(1)

}
package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.model.{SupportFile, TypeMapper, TemplateFile}
import java.io.File

trait GeneratorConfig {
  protected def destinationDir: String
  protected def sourceDir: String
  protected def templateDir: String

  protected def apiPackage: Option[String]
  protected def apiTemplateFiles: List[TemplateFile]

  protected def modelPackage: Option[String]
  protected def modelTemplateFiles: List[TemplateFile]

  protected def supportFiles: List[SupportFile]
  protected def typeMapping: List[TypeMapper]

  protected def packageSeparator: String

  protected final def buildPath(pieces: Option[String]*): String = {
    pieces.flatten.mkString(File.separator)
  }

  implicit def stringToOption(s: String): Option[String] = {
    if (s == null || s.isEmpty) None else Some(s)
  }
}

trait BasicGeneratorConfig extends GeneratorConfig {
  override protected def packageSeparator: String = "."
  override protected def destinationDir: String = "generated-code"
  override protected def sourceDir: String = "src"

  override protected def modelPackage: Option[String] = None
  override protected def apiPackage: Option[String] = None

  override protected def supportFiles: List[SupportFile] = Nil
}
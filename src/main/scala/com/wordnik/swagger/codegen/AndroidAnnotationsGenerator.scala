package com.wordnik.swagger.codegen

object AndroidAnnotationsGenerator extends AndroidAnnotationsGenerator

class AndroidAnnotationsGenerator extends JavaGenerator {
  override protected def templateDir = "AndroidAnnotations"

  override protected def destinationDir: String = "generated-code/AndroidAnnotations"

  override protected def mapHttpMethod(name: String): String = {
    name.charAt(0).toUpper + name.substring(1).toLowerCase
  }
}

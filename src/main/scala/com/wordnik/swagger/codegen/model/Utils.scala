package com.wordnik.swagger.codegen.model

case class TemplateFile(file: String,
                        sufix: String,
                        filePath: Option[String] = None)

case class SupportFile(file: String,
                       filePath: Option[String] = None)

case class TypeMapper(name: String,
                      nameTo: String,
                      `package`: Option[String] = None,
                      defaultValue: Option[String] = None,
                      isPrimitive: Boolean = false,
                      isComplex: Boolean = false,
                      isContainer: Boolean = false)

trait TemplateInfo {
  def name: String
}

trait IterableNode {
  var hasPrev: Boolean = true
  var hasNext: Boolean = true
}

object IterableNode {
  def updateNodes[T <: IterableNode](nodes: List[T]) = {
    nodes.headOption.map(_.hasPrev = false)
    nodes.lastOption.map(_.hasNext = false)
    nodes
  }
}

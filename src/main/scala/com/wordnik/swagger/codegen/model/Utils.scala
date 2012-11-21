package com.wordnik.swagger.codegen.model

trait TemplateInfo {
  def name: String
}

case class TemplateFile(file: String,
                        sufix: String,
                        filePath: Option[String] = None)

case class SupportFile(file: String,
                       name: String,
                       filePath: Option[String] = None)

case class TypeMapper(name: String,
                      nameTo: String,
                      `package`: Option[String] = None,
                      defaultValue: Option[String] = None,
                      isPrimitive: Boolean = false)

trait ListNode {
  var hasPrev: Boolean = true
  var hasNext: Boolean = true
}

object ListNode {
  def updateNodes[T <: ListNode](nodes: List[T]) = {
    nodes.headOption.map(_.hasPrev = false)
    nodes.lastOption.map(_.hasNext = false)
    nodes
  }
}

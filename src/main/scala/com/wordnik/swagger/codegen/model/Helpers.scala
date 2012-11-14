package com.wordnik.swagger.codegen.model

case class TemplateFile(file: String,
                        sufix: String)

case class TypeMapper(name: String,
                      nameTo: String,
                      `package`: Option[String] = None,
                      defaultValue: Option[String] = None)

trait TemplateInfo {
  def name: String
  def path: String
}

case class ModelInfo(name: String,
                     path: String,
                     `package`: String,
                     properties: List[PropertyInfo],
                     imports: Set[ImportInfo]
                      ) extends TemplateInfo

case class ApiInfo(name: String,
                   path: String
                    ) extends TemplateInfo

case class ImportInfo(`import`: String)

case class PropertyInfo(name: String,
                        `type`: String,
                        getter: String,
                        setter: String,
                        required: Boolean,
                        description: Option[String] = None) {
}


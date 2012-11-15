package com.wordnik.swagger.codegen.model

case class TemplateFile(file: String,
                        sufix: String,
                        filePath: Option[String] = None)

case class SupportFile(file: String,
                       name: String,
                       filePath: Option[String] = None)

case class TypeMapper(name: String,
                      nameTo: String,
                      `package`: Option[String] = None,
                      defaultValue: Option[String] = None)

trait TemplateInfo {
  def name: String
}

case class ModelInfo(name: String,
                     `package`: Option[String],
                     properties: List[PropertyInfo],
                     imports: Set[ImportInfo]
                      ) extends TemplateInfo

case class ApiInfo(name: String,
                   `package`: Option[String],
                   basePath: String,
                   operations: List[OperationInfo],
                   imports: Set[ImportInfo]
                    ) extends TemplateInfo

case class TypeInfo(name: String,
                    `package`: Option[String],
                    `import`: Option[String],
                    dataRef: Option[TypeInfo] = None)

case class ImportInfo(`import`: String)

case class PropertyInfo(name: String,
                        `type`: TypeInfo,
                        getter: String,
                        setter: String,
                        required: Boolean,
                        description: Option[String] = None)

case class OperationInfo(name: String,
                         path: String,
                         httpMethod: String,
                         returnType: TypeInfo,
                         parameters: List[ParameterInfo],
                         errorResponses: List[ErrorInfo],
                         description: Option[String] = None,
                         notes: Option[String] = None)

case class ParameterInfo(name: String,
                         `type`: TypeInfo,
                         paramType: String,
                         required: Boolean,
                         defaultValue: Option[String] = None,
                         description: Option[String] = None)

case class ErrorInfo(code: Int,
                     message: String)
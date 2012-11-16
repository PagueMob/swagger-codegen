package com.wordnik.swagger.codegen.model

import com.wordnik.swagger.codegen.util.SwaggerParamType

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
                    isPrimitive: Boolean = false,
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
                         notes: Option[String] = None) {
  lazy val pathParameters = filterParameters(SwaggerParamType.Path)
  lazy val queryParameters = filterParameters(SwaggerParamType.Query)
  lazy val bodyParameters = filterParameters(SwaggerParamType.Body)
  lazy val headerParameters = filterParameters(SwaggerParamType.Header)

  private def filterParameters(paramType: String) = {
    val r = parameters.filter(_.paramType == SwaggerParamType.Path)
    r.lastOption.map(e => r.updated(r.length-1, e.copy(hasNext = false))).getOrElse(Nil)
  }
}

case class ParameterInfo(name: String,
                         `type`: TypeInfo,
                         paramType: String,
                         required: Boolean,
                         defaultValue: Option[String] = None,
                         description: Option[String] = None,
                         hasNext: Boolean = true)

case class ErrorInfo(code: Int,
                     message: String)
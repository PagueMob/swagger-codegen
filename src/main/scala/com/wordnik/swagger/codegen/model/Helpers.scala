package com.wordnik.swagger.codegen.model

import com.wordnik.swagger.codegen.util.SwaggerParamType

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
                        description: Option[String] = None
                         ) extends ListNode

case class ParameterInfo(name: String,
                         `type`: TypeInfo,
                         paramType: String,
                         required: Boolean,
                         defaultValue: Option[String] = None,
                         description: Option[String] = None
                          ) extends ListNode

case class ErrorInfo(code: Int,
                     message: String)

case class OperationInfo(name: String,
                         path: String,
                         httpMethod: String,
                         returnType: TypeInfo,
                         parameters: List[ParameterInfo],
                         errorResponses: List[ErrorInfo],
                         description: Option[String] = None,
                         notes: Option[String] = None) {
  lazy val pathParams = filterParameters(SwaggerParamType.Path)
  lazy val queryParams = filterParameters(SwaggerParamType.Query)
  lazy val bodyParams = filterParameters(SwaggerParamType.Body)
  lazy val headerParams = filterParameters(SwaggerParamType.Header)

  private def filterParameters(paramType: String) = {
    ListNode.updateNodes(parameters.filter(_.paramType == paramType))
  }
}
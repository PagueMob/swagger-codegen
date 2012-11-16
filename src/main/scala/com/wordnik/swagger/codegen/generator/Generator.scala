package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.model._
import com.wordnik.swagger.codegen.util.Extractor
import com.wordnik.swagger.core._

trait Generator {
  this: GeneratorMapper with GeneratorWriter with GeneratorConfig =>
  def generate(basePath: String, apiKey: Option[String] = None)
}

trait BasicGenerator extends Generator {
  this: GeneratorMapper with GeneratorWriter with GeneratorConfig =>

  final def main(args: Array[String]) {
    require(!args.isEmpty, "Need url to resources as argument")
    val basePath = args(0)
    val apiKey = if (args.length > 1) Some(args(1)) else None
    generate(basePath, apiKey)
    System.exit(0)
  }

  final def generate(basePath: String, apiKey: Option[String] = None) {
    val doc = Extractor.fetchDocumentation(basePath, apiKey)
    val apis = Extractor.fetchResources(doc, apiKey)
    val models = Extractor.fetchModels(apis)

    val modelInfos = processModels(models)
    val apiInfos = processApis(apis)

    val outputPath = buildPath(destinationDir, sourceDir)
    writeTemplate(outputPath, modelTemplateFiles, modelInfos: _*)
    writeTemplate(outputPath, apiTemplateFiles, apiInfos: _*)
    copySupportFile(destinationDir, supportFiles)
    println("Generated Swagger Codegen.")
  }

  private def processModels(models: List[DocumentationSchema]): List[ModelInfo] = {
    models.map(model => buildModelInfo(model))
  }

  private def buildModelInfo(model: DocumentationSchema) = {
    val properties = buildProperties(convertJavaToScala(model.getProperties()))
    new ModelInfo(
      mapModelName(model.getId()),
      modelPackage,
      properties,
      buildImports(extractImportProperty(properties))
    )
  }

  private def buildProperties(properties: Map[String, DocumentationSchema]): List[PropertyInfo] = {
    properties.map {
      case (id, p) =>
        val kind = p.getType
        val typeRef = Option(p.getItems()).map(i => Some(i.ref)).getOrElse(None)
        new PropertyInfo(
          mapPropertyName(id, kind),
          mapType(kind, typeRef),
          mapGetterName(id, kind),
          mapSetterName(id, kind),
          p.getRequired(),
          Option(p.getDescription()))
    }.toList
  }

  private def processApis(apis: List[Documentation]): List[ApiInfo] = {
    apis.map(api => buildApiInfo(api))
  }

  private def buildApiInfo(api: Documentation) = {
    val op = convertJavaToScala(api.getApis()).map {
      a => a.getPath -> convertJavaToScala(a.getOperations())
    }.toMap
    val operations = buildOperations(op)
    new ApiInfo(
      mapApiName(api.getResourcePath),
      apiPackage,
      api.getBasePath,
      operations,
      buildImports(extractImportOperation(operations))
    )
  }

  private def buildOperations(operations: Map[String, List[DocumentationOperation]]): List[OperationInfo] = {
    operations.map {
      case (path, operation) =>
        operation.map {
          op =>
            OperationInfo(
              mapMethodName(op.getNickname),
              path,
              mapHttpMethod(op.getHttpMethod),
              mapType(op.getResponseClass()),
              buildParameters(convertJavaToScala(op.getParameters())),
              buildErrors(convertJavaToScala(op.getErrorResponses())),
              Option(op.getSummary),
              Option(op.getNotes)
            )
        }
    }.flatten.toList
  }

  private def buildParameters(parameters: List[DocumentationParameter]): List[ParameterInfo] = {
    val r = parameters.map {
      p =>
        ParameterInfo(
          p.getName,
          mapType(p.getDataType()),
          p.getParamType,
          p.getRequired,
          p.getDefaultValue,
          Option(p.getDescription)
        )
    }
    r.lastOption.map(e => r.updated(r.length-1, e.copy(hasNext = false))).getOrElse(Nil)
  }

  private def buildErrors(errors: List[DocumentationError]): List[ErrorInfo] = {
    errors.map(e => ErrorInfo(e.getCode, e.getReason))
  }

  private def extractImportProperty(properties: List[PropertyInfo]): List[TypeInfo] = {
    properties.map(p => p.`type`)
  }

  private def extractImportOperation(operations: List[OperationInfo]): List[TypeInfo] = {
    operations.map(
      o => o.returnType :: o.parameters.map(p => p.`type`)
    ).flatten
  }

  private def buildImports(types: List[TypeInfo]): Set[ImportInfo] = {
    val t = types.map {
      t => List(t.`import`) ++ t.dataRef.map(d => List(d.`import`)).getOrElse(Nil)
    }.flatten.flatten.toSet
    t.map(s => ImportInfo(s))
  }

  private def convertJavaToScala[T](list: java.util.List[T]): List[T] = {
    import scala.collection.JavaConversions._
    Option(list).map(_.toList).getOrElse(List.empty)
  }

  private def convertJavaToScala[V, K](map: java.util.Map[V, K]): Map[V, K] = {
    import scala.collection.JavaConversions._
    Option(map).map(_.toMap).getOrElse(Map.empty)
  }
}

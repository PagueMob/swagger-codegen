package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.model.{ImportInfo, PropertyInfo, ApiInfo, ModelInfo}
import com.wordnik.swagger.codegen.util.Extractor
import com.wordnik.swagger.core.{Documentation, DocumentationSchema}
import java.io.File
import scala.collection.JavaConversions._

trait Generator {
  def generate(basePath: String, apiKey: Option[String] = None)
}

trait BasicGenerator extends Generator {
  this: GeneratorWriter with GeneratorConfig =>

  final def main(args: Array[String]) {
    require(!args.isEmpty, "Need url to resources as argument")
    val basePath = args(0)
    val apiKey = if (args.length > 1) Some(args(1)) else None
    generate(basePath, apiKey)
  }

  final def generate(basePath: String, apiKey: Option[String] = None) {
    val doc = Extractor.fetchDocumentation(basePath, apiKey)
    val apis = Extractor.fetchResources(doc, apiKey)
    val models = Extractor.fetchModels(apis)

    val modelInfos = processModels(models)
    val apiInfos = processApis(apis)

    val outputPath = List(generatorDir, sourceDir).mkString(File.separator)
    writeTemplate(outputPath, modelTemplateFiles, modelInfos: _*)
    //writeTemplate(outputPath, apiTemplateFiles, apiInfos: _*)

    println("Generated Swagger Codegen.")
  }

  private def processModels(models: List[DocumentationSchema]): List[ModelInfo] = {
    models.map(model => buildModelInfo(model))
  }

  private def buildModelInfo(model: DocumentationSchema) = {
    val name = model.getId()
    val properties = buildProperties(model.getProperties().toMap)
    val imports = buildImports(processModelImport(model))
    new ModelInfo(name, modelPackage, modelPackage, properties, imports)
  }

  private def buildProperties(properties: Map[String, DocumentationSchema]): List[PropertyInfo] = {
    properties.map {
      case (id, p) =>
        val kind = p.getType
        new PropertyInfo(
          mapPropertyName(id, kind),
          mapTypeName(kind),
          mapGetterName(id, kind),
          mapSetterName(id, kind),
          p.getRequired(),
          Option(p.getDescription()))
    }.toList
  }

  private def processModelImport(model: DocumentationSchema): Set[String] = {
    model.getProperties().toMap.map {
      case (id, p) => p.getType
    }.toSet
  }

  private def processApis(apis: List[Documentation]): List[ApiInfo] = {
    apis.map(api => buildApiInfo(api))
  }

  private def buildApiInfo(api: Documentation) = {
    new ApiInfo(api.resourcePath, apiPackage)
  }

  private def buildImports(types: Set[String]): Set[ImportInfo] = {
    types.map(mapImport(_)).flatten.map(ImportInfo(_))
  }
}
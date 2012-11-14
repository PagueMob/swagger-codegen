package com.wordnik.swagger.codegen.util

import com.wordnik.swagger.core.{DocumentationEndPoint, DocumentationSchema, Documentation}
import scala.collection.JavaConversions._
import scala.io.Source

object Extractor {
  private lazy val mapper = JsonUtil.mapper

  def fetchDocumentation(path: String, apiKey: Option[String] = None): Documentation = {
    val doc = fetch(path, apiKey)
    mapper.readValue(doc, classOf[Documentation])
  }

  def fetchResources(doc: Documentation, apiKey: Option[String] = None): List[Documentation] = {
    doc.getApis().toList.map {
      ep =>
        val basePath = doc.getBasePath + ep.path.replaceAll(".\\{format\\}", ".json")
        val api = fetch(basePath, apiKey)
        mapper.readValue(api, classOf[Documentation])
    }
  }

  def fetchModels(resources: List[Documentation]): List[DocumentationSchema] = {
    val m = for (
      ep <- resources if (ep.getModels() != null)
    ) yield ep.getModels().toMap
    m.flatMap(_.values())
  }

  def fetchApis(resources: List[Documentation]): List[DocumentationEndPoint] = {
    val m = for (
      ep <- resources if (ep.getApis() != null)
    ) yield ep.getApis().toList
    m.flatten
  }

  private def fetch(path: String, apiKey: Option[String] = None) = {
    path.startsWith("http") match {
      case true =>
        val url = path + apiKey.map(k => "?api_key=" + k).getOrElse("")
        Source.fromURL(url).mkString
      case false => Source.fromFile(path).mkString
    }
  }
}



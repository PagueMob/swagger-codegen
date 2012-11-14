package com.wordnik.swagger.codegen.generator

import com.wordnik.swagger.codegen.util.JsonUtil
import com.wordnik.swagger.codegen.model.{TemplateFile, TemplateInfo}
import java.io.{FileInputStream, InputStream, FileWriter, File}
import org.fusesource.scalate.{Template, TemplateSource, TemplateEngine}
import scala.io.Source

trait GeneratorWriter {
  this: GeneratorConfig =>
  def writeTemplate(outputPath: String, templateFiles: List[TemplateFile], infos: TemplateInfo*)
}

trait BasicGeneratorWriter extends GeneratorWriter {
  this: GeneratorConfig =>
  private lazy val mapper = JsonUtil.mapper
  private lazy val engine = new TemplateEngine(Some(new File(".")))

  def writeTemplate(outputPath: String, templateFiles: List[TemplateFile], infos: TemplateInfo*) {
    templateFiles.foreach {
      tf =>
        val templatePath = List(templateDir, tf.file).mkString(File.separator)
        val template = compileTemplate(templatePath)
        infos.foreach {
          i =>
            val filePath = List(outputPath, i.path, i.name + tf.sufix).mkString(File.separator)
            val content = buildTemplate(filePath, template, i)
            writeFile(filePath, content)
        }
    }
  }

  private def compileTemplate(path: String) = {
    val source = TemplateSource.fromText(path, getTemplateText(path))
    engine.compile(source)
  }

  private def buildTemplate(path: String, template: Template, info: TemplateInfo): String = {
    val map = convertTemplateInfo(info)
    engine.layout(path, template, map)
  }

  private def convertTemplateInfo(info: TemplateInfo): Map[String, Any] = {
    val json = mapper.writeValueAsString(info)
    mapper.readValue(json, classOf[Map[String, Any]])
  }

  private def getTemplateText(path: String): String = {
    val is = getClass.getClassLoader.getResourceAsStream(path) match {
      case is: InputStream => is
      case _ => new FileInputStream(path)
    }
    Source.fromInputStream(is).mkString
  }

  private def writeFile(path: String, content: String) {
    println("Writing File:" + path)
    new File(path).getParentFile.mkdirs
    val fw = new FileWriter(path, false)
    fw.write(content)
    fw.close()
  }
}
/**
 * Copyright 2012 Wordnik, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordnik.swagger.codegen

import com.wordnik.swagger.model.ModelProperty


object BasicFlashGenerator extends BasicFlashGenerator {
  def main(args: Array[String]) = generateClient(args)
}

class BasicFlashGenerator extends BasicGenerator {

  override def typeMapping = Map(
    "boolean" -> "Boolean",
    "string" -> "String",
    "int" -> "int",
    "float" -> "Number",
    "long" -> "Number",
    "double" -> "Number",
    "date" -> "Date",
    "object" -> "Object")

  override def defaultIncludes = Set(
    "Boolean",
    "String",
    "int",
    "Number",
    "Date",
    "Object")

  // template used for models
  modelTemplateFiles += "model.mustache" -> ".as"
  modelTemplateFiles += "modelList.mustache" -> "List.as"

  // template used for models
  apiTemplateFiles += "api.mustache" -> ".as"

  // file suffix
  override def fileSuffix = ".as"

  // location of templates
  override def templateDir = "src/main/resources/flash"

  // where to write generated
  def destinationRoot: String = "generated-code/flash"

  // where to write generated source code
  override def destinationDir = destinationRoot + "/src"

  override def toVarName(name: String): String = {
    name.charAt(0).toLower + name.substring(1)
  }

  override def toGetter(name: String, datatype: String) = {
    datatype match {
      case "boolean" => "get is" + name.charAt(0).toUpper + name.substring(1)
      case _ => "get " + name.charAt(0).toLower + name.substring(1)
    }
  }
  override def toSetter(name: String, datatype: String) = {
    "set " + name.charAt(0).toLower + name.substring(1)
  }

  override def toDefaultValue(dataType: String, obj: ModelProperty) = {
    dataType match {
      case "Boolean" => "false"
      case "int" => "0"
      case "Number" => "0.0"
      case "List" => "[]"
      case "Array" => "[]"
      case _ => "null"
    }
  }

  override def toDeclaredType(dt: String): String = {
    val declaredType = dt match {
      case x:String if x.contains("Array[") => "Array"
      case x:String if x.contains("List[") => "Array"
      case _ => dt
    }
    super.toDeclaredType(declaredType)
  }
  // supporting classes
  override def supportingFiles = List(
    ("ApiInvoker.as", destinationDir + "/com/wordnik/swagger/common", "ApiInvoker.as"),
    ("ApiUrlHelper.as", destinationDir + "/com/wordnik/swagger/common", "ApiUrlHelper.as"),
    ("ApiUserCredentials.as", destinationDir + "/com/wordnik/swagger/common", "ApiUserCredentials.as"),
    ("ListWrapper.as", destinationDir + "/com/wordnik/swagger/common", "ListWrapper.as"),
    ("SwaggerApi.as", destinationDir + "/com/wordnik/swagger/common", "SwaggerApi.as"),
    ("XMLWriter.as", destinationDir + "/com/wordnik/swagger/common", "XMLWriter.as"),

    ("ApiError.as", destinationDir + "/com/wordnik/swagger/exception", "ApiError.as"),
    ("ApiErrorCodes.as", destinationDir + "/com/wordnik/swagger/exception", "ApiErrorCodes.as"),

    ("ApiClientEvent.as", destinationDir + "/com/wordnik/swagger/event", "ApiClientEvent.as"),
    ("Response.as", destinationDir + "/com/wordnik/swagger/event", "Response.as"),

    ("build.properties", destinationRoot, "build.properties"),
    ("build.xml", destinationRoot, "build.xml"),
    ("AirExecutorApp-app.xml", destinationRoot + "/bin", "AirExecutorApp-app.xml"),

    ("ASAXB-0.1.1.swc", destinationRoot + "/lib", "ASAXB-0.1.1.swc"),
    ("as3corelib.swc", destinationRoot + "/lib/ext", "as3corelib.swc"),
    ("flexunit-4.1.0_RC2-28-flex_3.5.0.12683.swc", destinationRoot + "/lib/ext", "flexunit-4.1.0_RC2-28-flex_3.5.0.12683.swc"),
    ("flexunit-aircilistener-4.1.0_RC2-28-3.5.0.12683.swc", destinationRoot + "/lib/ext", "flexunit-aircilistener-4.1.0_RC2-28-3.5.0.12683.swc"),
    ("flexunit-cilistener-4.1.0_RC2-28-3.5.0.12683.swc", destinationRoot + "/lib/ext", "flexunit-cilistener-4.1.0_RC2-28-3.5.0.12683.swc"),
    ("flexunit-core-flex-4.0.0.2-sdk3.5.0.12683.swc", destinationRoot + "/lib/ext", "flexunit-core-flex-4.0.0.2-sdk3.5.0.12683.swc")
  )
}
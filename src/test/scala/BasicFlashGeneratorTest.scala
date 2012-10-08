/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.wordnik.swagger.model._
import com.wordnik.swagger.codegen.{BasicFlashGenerator, BasicScalaGenerator, Codegen, PathUtil}
import com.wordnik.swagger.codegen.util._
import com.wordnik.swagger.codegen.language._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.HashMap

@RunWith(classOf[JUnitRunner])
class BasicFlashGeneratorTest extends FlatSpec with ShouldMatchers {
  val json = ScalaJsonUtil.getJsonMapper

  val config = new BasicFlashGenerator

  behavior of "BasicFlashGenerator"

  /*
   * types are mapped between swagger types and language-specific
   * types
   */
  it should "convert to a declared type" in {
    config.toDeclaredType("boolean") should be ("Boolean")
    config.toDeclaredType("string") should be ("String")
    config.toDeclaredType("int") should be ("int")
    config.toDeclaredType("float") should be ("Number")
    config.toDeclaredType("long") should be ("Number")
    config.toDeclaredType("double") should be ("Number")
    config.toDeclaredType("object") should be ("Object")
  }
}
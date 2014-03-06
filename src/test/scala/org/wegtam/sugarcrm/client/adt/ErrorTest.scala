package org.wegtam.sugarcrm.client.adt

import org.scalatest.{Matchers, FunSpec}
import argonaut._, Argonaut._
import scalaz._

/**
 * Tests for the sugarcrm api error.
 */
class ErrorTest extends FunSpec with Matchers {
  describe("asJson") {
    it("should return the correct json") {
      val e = new Error("foo", 42, "What is the meaning of life?")
      e.asJson.toString() should be ("""{"name":"foo","number":42,"description":"What is the meaning of life?"}""")
    }
  }
  describe("decode") {
    describe("when given valid json") {
      it("should return the correct Error object") {
        val e = new Error("foo", 42, "What is the meaning of life?")
        val json = """{"name":"foo","number":42,"description":"What is the meaning of life?"}"""
        val r: String \/ Error = Parse.decodeEither[Error](json)
        r.isRight should be (true)
        val actual = r | None
        actual should be (e)
      }
    }
  }
}

package org.wegtam.utils.security

import org.scalatest.{Matchers, FunSpec}

/**
 * Tests for the hash helper functions.
 */
class Hash$Test extends FunSpec with Matchers {
  describe("MD5") {
    describe("when given an empty string") {
      it("should return d41d8cd98f00b204e9800998ecf8427e") {
        Hash.MD5("") should be("d41d8cd98f00b204e9800998ecf8427e")
      }
    }
    describe("when given a non-empty string") {
      it("should return the correct md5 sum") {
        Hash.MD5("I am just a lonely string.") should be("bdbcb65a595735ad6a903d22c5ec9283")
      }
    }
  }

  describe("SHA1") {
    describe("when given an empty string") {
      it("should return da39a3ee5e6b4b0d3255bfef95601890afd80709") {
        Hash.SHA1("") should be("da39a3ee5e6b4b0d3255bfef95601890afd80709")
      }
    }
    describe("when given a non-empty string") {
      it("should return the correct sha1 sum") {
        Hash.SHA1("I am just a lonely string.") should be("8875114d6de4a755ecc2b085a58bbccae1c1379e")
      }
    }
  }

  describe("SHA256") {
    describe("when given an empty string") {
      it("should return e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855") {
        Hash.SHA256("") should be("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
      }
    }
    describe("when given a non-empty string") {
      it("should return the correct sha256 sum") {
        Hash.SHA256("I am just a lonely string.") should be("553e1332affdb1171a949b0fdf3dc340acac60e59ebeca262de0c537983971c4")
      }
    }
  }

  describe("SHA512") {
    describe("when given an empty string") {
      it("should return cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e") {
        Hash.SHA512("") should be("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e")
      }
    }
    describe("when given a non-empty string") {
      it("should return the correct sha512 sum") {
        Hash.SHA512("I am just a lonely string.") should be("8bfd47de6abd520d6c4f8539867373c7df56946953d8cba97e5c23adbb95cbfe1968ce9356fbe1882b20e03c37481bd1c9fed3e498b9762eaa4c133447a18caf")
      }
    }
  }
}

package org.wegtam.utils.security

import org.scalatest.{Matchers, FunSpec}
import java.io.File

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
    describe("when given an empty file") {
      it("should return d41d8cd98f00b204e9800998ecf8427e") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMeEmpty.txt").getPath)
        Hash.MD5(f) should be("d41d8cd98f00b204e9800998ecf8427e")
      }
    }
    describe("when given a non-empty file") {
      it("should return the correct md5 sum") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMe.txt").getPath)
        Hash.MD5(f) should be("c83f624f6d008b8b37a838bce1316228")
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
    describe("when given an empty file") {
      it("should return da39a3ee5e6b4b0d3255bfef95601890afd80709") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMeEmpty.txt").getPath)
        Hash.SHA1(f) should be("da39a3ee5e6b4b0d3255bfef95601890afd80709")
      }
    }
    describe("when given a non-empty file") {
      it("should return the correct sha1 sum") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMe.txt").getPath)
        Hash.SHA1(f) should be("163ffd47d184fca7cdc8da81eaf96b6a2331b21d")
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
    describe("when given an empty file") {
      it("should return e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMeEmpty.txt").getPath)
        Hash.SHA256(f) should be("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
      }
    }
    describe("when given a non-empty file") {
      it("should return the correct sha256 sum") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMe.txt").getPath)
        Hash.SHA256(f) should be("4876064f6109dc10f8b6f679ba835b57fc89f3921a333de81bc82a9c69f377e6")
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
    describe("when given an empty file") {
      it("should return cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMeEmpty.txt").getPath)
        Hash.SHA512(f) should be("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e")
      }
    }
    describe("when given a non-empty file") {
      it("should return the correct sha512 sum") {
        val f = new File(getClass.getResource("/org/wegtam/utils/security/HashMe.txt").getPath)
        Hash.SHA512(f) should be("66be2c69ca6b2efdf7edc85296bce7d4f037a0d65aeabf00e13a54de89211e8aa569713c506c0d55b81a12f1b0699b8b2610ca3728cb7ff3c95dbb1c2fa95154")
      }
    }
  }
}

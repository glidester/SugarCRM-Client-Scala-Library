package org.wegtam.utils.security

import java.security.MessageDigest

/**
 * Several helper functions for hashes.
 */
object Hash {
  def hashText(algorithm: String, text: String): String = {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(text.getBytes)
    bytes.map("%02X".format(_)).mkString.toLowerCase
  }

  def MD5(text: String): String = {
    hashText("MD5", text)
  }

  def SHA1(text: String): String = {
    hashText("SHA1", text)
  }

  def SHA256(text: String): String = {
    hashText("SHA-256", text)
  }

  def SHA512(text: String): String = {
    hashText("SHA-512", text)
  }
}

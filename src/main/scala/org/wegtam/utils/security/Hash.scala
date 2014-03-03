package org.wegtam.utils.security

import java.security.MessageDigest
import java.io.File
import java.io.FileInputStream

/**
 * Several helper functions for hashes.
 */
object Hash {
  /**
   * Calculate a hash using the specified algorithm on the given file.
   *
   * @param algorithm The algorithm (e.g. SHA-256).
   * @param file The file to hash.
   * @return The hashcode as string.
   */
  def hashFile(algorithm: String, file: File): String = {
    val digest = MessageDigest.getInstance(algorithm)
    val fis = new FileInputStream(file)
    val buffer = new Array[Byte](1024)
    var readBytes = 0
    while (readBytes != -1) {
      readBytes = fis.read(buffer)
      if (readBytes > 0) digest.update(buffer, 0, readBytes)
    }
    fis.close()
    digest.digest().map("%02X".format(_)).mkString.toLowerCase
  }

  /**
   * Calculate a hash using the specified algorithm on the given string.
   *
   * @param algorithm The algorithm (e.g. SHA-256).
   * @param text The content for which to create the hash.
   * @return The hashcode as string.
   */
  def hashText(algorithm: String, text: String): String = {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(text.getBytes)
    bytes.map("%02X".format(_)).mkString.toLowerCase
  }

  /**
   * Determine the correct hash function depending on the type of the content.
   *
   * @todo Implement a more elegant type solution (`Either`?) to allow compile time type checking!
   *
   * @param algorithm The algorithm (e.g. SHA-256).
   * @param content The content for which to create the hash.
   * @return The hashcode as string.
   * @throws IllegalArgumentException if the content type is not supported.
   */
  def hashContent(algorithm: String, content: Any): String = {
    val file = classOf[File]
    val string = classOf[String]

    content.getClass match {
      case `file` => hashFile(algorithm, content.asInstanceOf[File])
      case `string` => hashText(algorithm, content.asInstanceOf[String])
      case _ => throw new IllegalArgumentException(s"Type ${content.getClass} not supported!")
    }
  }

  def MD5(content: Any): String = {
    hashContent("MD5", content)
  }

  def SHA1(content: Any): String = {
    hashContent("SHA1", content)
  }

  def SHA256(content: Any): String = {
    hashContent("SHA-256", content)
  }

  def SHA512(content: Any): String = {
    hashContent("SHA-512", content)
  }
}

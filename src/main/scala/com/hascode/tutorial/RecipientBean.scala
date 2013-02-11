package com.hascode.tutorial
import org.apache.camel.RecipientList
import org.apache.camel.Header

class RecipientsBean {

  @RecipientList def getRecipients(@Header("CamelFileName") fileName: String): Array[String] = {
    println("detecting recipients for given filename: " + fileName)
    if (fileName.endsWith("csv"))
      Array("jms:csvQueue")
    Array("jms:defaultQueue")
  }
}

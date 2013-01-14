package com.hascode.tutorial
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport
import org.apache.camel.CamelContext
import com.icegreen.greenmail.user.GreenMailUser
import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetupTest
import javax.mail.internet.MimeMessage
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.Message

object ImapExample extends App {
  val mailServer = new GreenMail(ServerSetupTest.IMAP)
  mailServer.start
  val user = mailServer.setUser("test@hascode.com", "joe", "XXXX")

  val context: CamelContext = new DefaultCamelContext
  val routeBuilder = new RouteBuilder {
    from("imap://joe@0.0.0.0:3143?password=XXXX") --> ("file:data/outbox")
  }
  context.addRoutes(routeBuilder)
  context.start
  Thread.sleep(3000)
  for (i <- 0 until 3) {
    var message = new MimeMessage(null: Session)
    message.setFrom(new InternetAddress("test@hascode.com"))
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(
      "foo@hascode.com"))
    message.setSubject("Test E-Mail #" + i)
    message.setText("This is a fine test e-mail. It is the " + i + " message of 3.")
    println("sending new email: #" + i)
    user.deliver(message)
    Thread.sleep(2000)
  }

  while (true) {}
}


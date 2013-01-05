package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport

object ImapExample extends App {
  val context: CamelContext = new DefaultCamelContext
  val routeBuilder = new RouteBuilder {
    //TODO: use/bootstrap greenmail server
    from("imap://hostname[:port]") --> ("file:data/outbox");
  }
  context.addRoutes(routeBuilder)
  context.start
  while (true) {}
}


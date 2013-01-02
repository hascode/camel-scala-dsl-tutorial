package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext

object SimpleFileEndpointExample extends App {
  val context: CamelContext = new DefaultCamelContext
  context.addRoutes(new FileEndpointBuilder)
  context.start
  while (true) {}
}

class FileEndpointBuilder extends RouteBuilder {
  from("file:data/inbox") --> ("file:data/outbox")
}
package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport

object VelocityExample extends App {
  val context: CamelContext = new DefaultCamelContext
  val routeBuilder = new RouteBuilder {
    from("file:data/inbox") --> ("velocity:com/hascode/tutorial/transform.vm") --> ("file:data/outbox")
  }
  context.addRoutes(routeBuilder)
  context.start
  while (true) {}
}


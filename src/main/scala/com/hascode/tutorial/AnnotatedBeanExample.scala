package com.hascode.tutorial
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilder
import javax.jms.ConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.apache.activemq.ActiveMQConnectionFactory

object AnnotatedBeanExample extends App {
  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("vm://localhost")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

  val routeBuilder = new RouteBuilder {
    from("file:data/inbox").bean(classOf[RecipientsBean])

    from("jms:csvQueue").process(exchange => println("csv queue: file received: " + exchange.getIn().getHeader("CamelFileName")))
    from("jms:defaultQueue").process(exchange => println("default queue: file received: " + exchange.getIn().getHeader("CamelFileName")))
  }
  context.addRoutes(routeBuilder)
  context.start
  while (true) {}
}
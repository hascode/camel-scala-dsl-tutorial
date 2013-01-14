package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import javax.jms.ConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.Processor
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport

object ExampleWithMessagingUsingActiveMQ extends App with RouteBuilderSupport {
  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("vm://localhost")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))
  val routeBuilder = new RouteBuilder {
    from("file:data/inbox")
      .choice {
        when(_.in("CamelFileName") match {
          case x: String => if (x.endsWith(".xml")) -->("jms.xmlOrders") else -->("jms:csvOrders")
          case _ => -->("jms:invalidOrders")
        })
      }

    from("jms:xmlOrders")
      .process(exchange => println(exchange.getIn().getHeader("CamelFileName")))
  }
  context.addRoutes(routeBuilder)
  context.start
  while (true) {}
}


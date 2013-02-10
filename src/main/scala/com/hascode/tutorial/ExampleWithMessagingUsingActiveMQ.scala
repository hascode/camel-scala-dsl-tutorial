package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import javax.jms.ConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.Processor
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport
import org.apache.camel.Exchange

object ExampleWithMessagingUsingActiveMQ extends App with RouteBuilderSupport {
  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("vm://localhost")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))
  val routeBuilder = new RouteBuilder {
    from("file:data/inbox")
      .choice()
      .when(fileEndsWith(_, "xml")).to("jms:xmlOrders")
      .when(fileEndsWith(_, "csv")).to("jms:csvOrders")
      .otherwise().to("jms:unknownOrders")

    from("jms:xmlOrders")
      .process(exchange => println("XML type order received: " + exchange.getIn().getHeader("CamelFileName")))
    from("jms:csvOrders")
      .process(exchange => println("CSV type order received: " + exchange.getIn().getHeader("CamelFileName")))
    from("jms:unknownOrders")
      .process(exchange => println("Unknown type order received: " + exchange.getIn().getHeader("CamelFileName")))
  }
  context.addRoutes(routeBuilder)
  context.start
  while (true) {}

  def fileEndsWith(ex: Exchange, fileExt: String): Boolean = {
    ex.getIn().getHeader("CamelFileName") match {
      case x: String => return x.endsWith(fileExt)
      case _ => false
    }
  }
}


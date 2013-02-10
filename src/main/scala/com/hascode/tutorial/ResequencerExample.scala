package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import javax.jms.ConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.Processor
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport

object ResequencerExample extends App with RouteBuilderSupport {
  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("vm://localhost")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))

  val routeBuilder = new RouteBuilder {
    from("jms:wip").resequence(header("custom-priority")).batch().timeout(10000).reverse().to("jms:wip-inorder")

    from("jms:wip-inorder")
      .process(exchange => println("Message received with priority: " + exchange.getIn().getHeader("custom-priority")))
  }
  context.addRoutes(routeBuilder)
  context.start

  Thread.sleep(2000)
  val tpl = context.createProducerTemplate
  tpl.setDefaultEndpointUri("jms:wip")
  tpl.sendBodyAndHeader("foo1", "custom-priority", 1)
  tpl.sendBodyAndHeader("foo2", "custom-priority", 5)
  tpl.sendBodyAndHeader("foo3", "custom-priority", 6)
  tpl.sendBodyAndHeader("foo4", "custom-priority", 3)
  tpl.sendBodyAndHeader("foo5", "custom-priority", 4)
  tpl.sendBodyAndHeader("foo6", "custom-priority", 2)
  while (true) {}
}


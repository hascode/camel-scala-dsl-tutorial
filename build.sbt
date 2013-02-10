name := "camel-scala-tutorial"

organization := "com.hascode.tutorial"

version := "1.0.0"

libraryDependencies ++= Seq(
	"org.apache.camel" % "camel-scala" % "2.10.1",
	"org.apache.activemq" % "activemq-core" % "5.6.0",
	"org.apache.camel" % "camel-jms" % "2.10.1",
	"ch.qos.logback" % "logback-core" % "1.0.9",
	"ch.qos.logback" % "logback-classic" % "1.0.9",
	"javax.mail" % "mail" % "1.4.5",
	"com.icegreen" % "greenmail" % "1.3",
	"org.apache.camel" % "camel-mail" % "2.10.3"
)

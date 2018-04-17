resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.bintrayRepo("beyondthelines", "maven")

addSbtPlugin("io.frees"         % "sbt-frees-rpc-idlgen" % "0.13.2")
addSbtPlugin("org.lyranthe.sbt" % "partial-unification"  % "1.1.0")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

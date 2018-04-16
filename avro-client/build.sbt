
lazy val freesRpc            = "0.13.4-SNAPSHOT"
lazy val scalameta           = "1.8.0"
lazy val protocolVersion     = "1-SNAPSHOT"

lazy val srcGenSettings = Seq(
  idlType := "avro",
  srcGenSerializationType := "AvroWithSchema",
  srcJarNames := Seq("avro-protocol"),
  srcGenSourceDir := (Compile / sourceManaged).value / "avro",
  srcGenTargetDir := (Compile / sourceManaged).value / "compiled_avro",
  sourceGenerators in Compile += (srcGenFromJars in Compile).taskValue,
)

lazy val `avro-client` = project
  .in(file("."))
  .settings(name := "avro-client")
  .settings(scalaVersion := "2.12.4")
  .settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.bintrayRepo("beyondthelines", "maven")
    ),
    libraryDependencies ++= Seq(
      "io.frees"      %% "frees-rpc-client-core"  % freesRpc,
      "io.frees"      %% "frees-rpc-client-netty" % freesRpc,
      "com.example"   %% "avro-protocol"          % protocolVersion,
      "org.scalameta" %% "scalameta"              % scalameta
    ),
    srcGenSettings,
    addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
    scalacOptions += "-Xplugin-require:macroparadise",
    scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise")) // macroparadise plugin doesn't work in repl yet.
  )


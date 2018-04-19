
val freestyle           = "0.8.0"
val freestyleRPCVersion = "0.13.3"
val log4sVersion        = "1.4.0"
val shapelessVersion    = "2.3.2"


lazy val commonSettings = Seq(
  organization := "com.example",
  scalaVersion := "2.12.4",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import",
    "-Ypartial-unification"
  ),
  publishArtifact in (Compile, packageDoc) := false,
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.8.0" % Provided,
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise"))
)

lazy val commonProtocolSettings: Seq[Def.Setting[_]] = Seq(
  libraryDependencies ++= Seq(
    "io.frees"    %% "frees-rpc-client-core" % freestyleRPCVersion,
    "com.chuusai" %% "shapeless"             % shapelessVersion
  )
)

lazy val avroSrcGenSettings: Seq[Def.Setting[_]] = Seq(
  publishMavenStyle := true,
  //mappings in (Compile, packageBin) ~= { _.filter(!_._1.getName.endsWith(".class")) },
  idlType := "avro",
  srcGenSerializationType := "AvroWithSchema",
  srcGenSourceDir := (Compile / resourceDirectory).value,
  srcGenTargetDir := (Compile / sourceManaged).value / "compiled_avro",
  sourceGenerators in Compile += (srcGen in Compile).taskValue,
)

lazy val `avro-protocol` =
  project
    .in(file("avro-protocol"))
    .settings(commonSettings)
    .settings(commonProtocolSettings)
    .settings(avroSrcGenSettings)

lazy val `protobuf-protocol` =
  project
    .in(file("protobuf-protocol"))
    .settings(commonSettings)
    .settings(commonProtocolSettings)

lazy val `server` =
  project
    .in(file("server"))
    .settings(name := "server")
    .settings(commonSettings)
    .dependsOn(`avro-protocol`)
    .dependsOn(`protobuf-protocol`)
    .settings(
      libraryDependencies ++= Seq(
        "io.frees"  %% "frees-rpc-server" % freestyleRPCVersion,
        "org.log4s" %% "log4s"            % log4sVersion,
        "org.slf4j"  % "slf4j-simple"     % "1.7.25"
      ))

lazy val root =
  project
    .in(file("."))
    .settings(name := "freestyle-rpc-demo")
    .settings(commonSettings)
    .aggregate(`avro-protocol`, `protobuf-protocol`, `server`)



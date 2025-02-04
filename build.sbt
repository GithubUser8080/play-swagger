
organization in ThisBuild := "com.iheart"



lazy val noPublishSettings = Seq(
  skip in publish := true,
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val scalaV = "2.12.14"

lazy val root = project.in(file("."))
  .aggregate(playSwagger, sbtPlaySwagger)
  .settings(
    Publish.coreSettings,
    sourcesInBase := false,
    noPublishSettings,
      
    scalaVersion := scalaV
  )

lazy val playSwagger = project.in(file("core"))
  .settings(
    Publish.coreSettings,
    Format.settings,
    Testing.settings,
    name := "play-swagger",
    libraryDependencies ++= Dependencies.playTest ++
      Dependencies.playRoutesCompiler ++
      Dependencies.playJson ++
      Dependencies.enumeratum ++
      Dependencies.test ++
      Dependencies.yaml,
    scalaVersion := scalaV,
    crossScalaVersions := Seq(scalaVersion.value, "2.13.6")
  )

lazy val sbtPlaySwagger = project.in(file("sbtPlugin"))
  .settings(
    Publish.coreSettings,
    Format.settings,
    addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.17" % Provided),
    addSbtPlugin("com.typesafe.sbt" %% "sbt-web" % "1.4.3" % Provided))
  .enablePlugins(BuildInfoPlugin, SbtPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "com.iheart.playSwagger",
    name := "sbt-play-swagger",
    description := "sbt plugin for play swagger spec generation",
    sbtPlugin := true,
    scalaVersion := scalaV,
    scripted := scripted.dependsOn(publishLocal in playSwagger).evaluated,
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )


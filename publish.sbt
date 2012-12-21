publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo <<= (version) { version: String =>
    val sonatype = "https://oss.sonatype.org/"
    if (version.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at sonatype + "content/repositories/snapshots")
    else
        Some("releases"  at sonatype + "service/local/staging/deploy/maven2")
}

credentials += Credentials(Path.userHome / ".credentials" / "sonatype")

pomExtra := PomExtra(
    url = "https://github.com/dadrox/sbt-junit",
    scm = Scm("https://github.com/dadrox/sbt-junit", "scm:git:git@github.com:dadrox/sbt-junit.git"),
    license = License("BSD 2-Clause License", "http://opensource.org/licenses/BSD-2-Clause"),
    developers = List(
        Developer("dadrox", "Christopher Wood", "https://github.com/dadrox")))

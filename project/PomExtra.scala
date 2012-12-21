case class Developer(id: String, name: String, url: String)
case class License(name: String, url: String)
case class Scm(url: String, connection: String)

object PomExtra {
    def apply(url: String, scm: Scm, license: License, developers: Seq[Developer]) =
<url>{url}</url>
<licenses>
  <license>
    <name>{license.name}</name>
    <url>{license.url}</url>
  </license>
</licenses>
<scm>
  <url>{scm.url}</url>
  <connection>{scm.connection}</connection>
</scm>
<developers>{
  developers.map { dev =>
  <developer>
    <id>{dev.id}</id>
    <name>{dev.name}</name>
    <url>{dev.url}</url>
  </developer>
  }
}
</developers>
}


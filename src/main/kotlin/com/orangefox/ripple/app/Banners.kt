import org.springframework.boot.Banner
import org.springframework.core.env.Environment
import java.io.PrintStream

class MainBanner: Banner {
  override fun printBanner(environment: Environment?, sourceClass: Class<*>?, out: PrintStream?) {
    val banner = """
      \______   \__|_____ ______ |  |   ____
       |       _/  \____ \\____ \|  | _/ __ \
       |    |   \  |  |_> >  |_> >  |_\  ___/
       |____|_  /__|   __/|   __/|____/\___  >
              \/   |__|   |__|             \/
    """.trimIndent()
    out?.println(banner)
  }

}

class ConsolidationBanner: Banner {
  override fun printBanner(environment: Environment, sourceClass: Class<*>, out: PrintStream) {
    val banner = """
      \______   \__|_____ ______ |  |   ____
       |       _/  \____ \\____ \|  | _/ __ \
       |    |   \  |  |_> >  |_> >  |_\  ___/
       |____|_  /__|   __/|   __/|____/\___  >
              \/   |__|   |__|             \/
                   Consolidation
    """.trimIndent()
    out.println(banner)
  }

}

class ReservationBanner: Banner {
  override fun printBanner(environment: Environment, sourceClass: Class<*>?, out: PrintStream) {
    val banner = """
      \______   \__|_____ ______ |  |   ____
       |       _/  \____ \\____ \|  | _/ __ \
       |    |   \  |  |_> >  |_> >  |_\  ___/
       |____|_  /__|   __/|   __/|____/\___  >
              \/   |__|   |__|             \/
                    Reservations
    """.trimIndent()
    out.println(banner)
  }

}

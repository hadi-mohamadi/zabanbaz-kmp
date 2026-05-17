package ir.startup.zabanbaz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "KotlinTelegramBot"
include("src:main:KotlinTelegramBot")
findProject(":src:main:KotlinTelegramBot")?.name = "KotlinTelegramBot"

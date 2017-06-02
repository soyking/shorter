import java.io.FileInputStream
import java.util.*

val props: Properties = Properties()

fun main(args: Array<String>) {
    val appConfFile = FileInputStream("src/conf/app.conf")
    props.load(appConfFile)
    appConfFile.close()

    router.init()
}
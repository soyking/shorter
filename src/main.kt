import java.util.*
import java.io.FileInputStream


fun main(args: Array<String>) {
    val props = Properties()
    val appConfFile = FileInputStream("src/conf/app.conf")
    props.load(appConfFile)
    appConfFile.close()

    router.init(props)
}
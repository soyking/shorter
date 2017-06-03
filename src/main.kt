import java.io.FileInputStream
import java.util.*


fun main(args: Array<String>) {
    val props: Properties = Properties()
    val appConfFile = FileInputStream("src/conf/app.conf")
    props.load(appConfFile)
    appConfFile.close()

    router.init(props)
}
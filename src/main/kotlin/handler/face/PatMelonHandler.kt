package top.e404.skiko.handler.face

import org.jetbrains.skia.Surface
import top.e404.skiko.apt.annotation.ImageHandler
import top.e404.skiko.frame.*
import top.e404.skiko.frame.HandleResult.Companion.result
import top.e404.skiko.handler.DrawData
import top.e404.skiko.util.*
import top.e404.skiko.util.getJarImage

/**
 * 拍
 */
@ImageHandler
object PatMelonHandler : FramesHandler {
    private const val w = 926
    private const val h = 650
    private val range = 0..12
    private val bgList = range.map { getJarImage("statistic/gua/$it.png") }
    private val ddList = DrawData.loadFromJar("statistic/gua/gua.yml")

    override val name = "瓜"
    override val regex = Regex("(?i)瓜|gua")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ): HandleResult {
        var i = 0
        frames.handle { subCenter() }
        val fs = range.map {
            i++
            if (i >= frames.size) i = 0
            frames[i].clone()
        }.toMutableList()
        return fs.result {
            common(args).pmapIndexed { index ->
                duration = 100
                handle {
                    Surface.makeRasterN32Premul(w, h).withCanvas {
                        ddList[index].draw(this, image)
                        drawImage(bgList[index], 0F, 0F)
                    }
                }
            }
        }
    }
}
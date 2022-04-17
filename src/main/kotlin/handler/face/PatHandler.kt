package top.e404.skiko.handler.face

import org.jetbrains.skia.Rect
import top.e404.skiko.apt.annotation.ImageHandler
import top.e404.skiko.frame.*
import top.e404.skiko.frame.HandleResult.Companion.result
import top.e404.skiko.util.getJarImage
import top.e404.skiko.util.round
import top.e404.skiko.util.toSurface
import top.e404.skiko.util.withCanvas

/**
 * 拍
 */
@ImageHandler
object PatHandler : FramesHandler {
    private val bg = getJarImage("statistic/pat.png")

    override val name = "拍"
    override val regex = Regex("(?i)拍|pai")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ) = frames.result {
        common(args).handle {
            val face = round()
            bg.toSurface().withCanvas {
                drawImage(bg, 0F, 0F)
                drawImageRect(face,
                    Rect.makeWH(face.width.toFloat(), face.height.toFloat()),
                    Rect.makeXYWH(230F, 270F, 150F, 150F))
            }
        }
    }
}
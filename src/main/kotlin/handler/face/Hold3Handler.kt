package top.e404.skiko.handler.face

import org.jetbrains.skia.Rect
import org.jetbrains.skia.Surface
import top.e404.skiko.apt.annotation.ImageHandler
import top.e404.skiko.frame.*
import top.e404.skiko.frame.HandleResult.Companion.result
import top.e404.skiko.util.drawImageRectNearest
import top.e404.skiko.util.getJarImage
import top.e404.skiko.util.withCanvas

@ImageHandler
object Hold3Handler : FramesHandler {
    private const val w = 222
    private const val h = 219
    private val range = 0..1
    private val cover = range.map { getJarImage("statistic/hold/3.$it.png") }
    private val dstRect = Rect.makeXYWH(53F, 143F, 95F, 95F)

    override val name = "Hold3"
    override val regex = Regex("(?i)Hold3")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ) = frames.common(args).replenish(2, Frame::limitAsGif).result {
        handleIndexed { index ->
            val src = Rect.makeWH(width.toFloat(), height.toFloat())
            Surface.makeRasterN32Premul(w, h).withCanvas {
                drawImageRectNearest(this@handleIndexed, src, dstRect) // face
                drawImage(cover[index % 2], 0F, 0F) // cover
            }
        }
    }
}
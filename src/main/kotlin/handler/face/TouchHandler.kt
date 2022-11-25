package top.e404.skiko.handler.face

import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Surface
import top.e404.skiko.Colors
import top.e404.skiko.apt.annotation.ImageHandler
import top.e404.skiko.frame.*
import top.e404.skiko.frame.HandleResult.Companion.result
import top.e404.skiko.handler.DrawData
import top.e404.skiko.util.getJarImage
import top.e404.skiko.util.round
import top.e404.skiko.util.withCanvas

@ImageHandler
object TouchHandler : FramesHandler {
    private const val w = 480
    private const val h = 270
    private val bgList = (0..9).map { getJarImage("statistic/touch/$it.png") }
    private val ddList = DrawData.loadFromJar("statistic/touch/touch.yml")
    private val bgRect = Rect.makeWH(w.toFloat(), h.toFloat())
    private val paint = Paint().apply { color = Colors.WHITE.argb }

    override val name = "Touch"
    override val regex = Regex("摸|(?i)mo|touch")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ) = frames.common(args).handle { it.round() }.replenish(10).result {
        handleIndexed { index ->
            val src = Rect.makeWH(width.toFloat(), height.toFloat())
            Surface.makeRasterN32Premul(w, h).withCanvas {
                val i = index % 10
                drawRect(bgRect, paint)
                ddList[i].draw(this, this@handleIndexed, src)
                drawImageRect(bgList[i], bgRect)
            }
        }
    }
}
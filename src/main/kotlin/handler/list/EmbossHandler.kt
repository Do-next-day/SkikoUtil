package top.e404.skiko.handler.list

import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.IRect
import top.e404.skiko.apt.annotation.ImageHandler
import top.e404.skiko.argb
import top.e404.skiko.frame.Frame
import top.e404.skiko.frame.FramesHandler
import top.e404.skiko.frame.HandleResult.Companion.result
import top.e404.skiko.frame.common
import top.e404.skiko.frame.handle
import top.e404.skiko.limit
import top.e404.skiko.rgb
import top.e404.skiko.util.toBitmap
import top.e404.skiko.util.toImage

/**
 * 浮雕效果
 */
@ImageHandler
object EmbossHandler : FramesHandler {
    override val name = "浮雕"
    override val regex = Regex("(?i)浮雕|fd")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ) = frames.result {
        common(args).handle {
            val old = toBitmap()
            val new = toBitmap()
            for (x in 1 until old.width - 1) for (y in 1 until old.height - 1) {
                new.erase(old.fd(x, y), IRect.makeXYWH(x, y, 1, 1))
            }
            new.toImage()
        }
    }

    private fun Bitmap.fd(x: Int, y: Int): Int {
        val c = getColor(x, y)
        val a = c shr 24
        if (a == 0) return c
        val (pr, pg, pb) = getColor(x - 1, y - 1).rgb()
        val (nr, ng, nb) = getColor(x + 1, y + 1).rgb()
        return argb(
            a,
            (nr - pr + 128).limit(),
            (ng - pg + 128).limit(),
            (nb - pb + 128).limit(),
        )
    }
}
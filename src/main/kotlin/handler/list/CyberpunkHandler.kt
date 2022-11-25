package top.e404.skiko.handler.list

import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.IRect
import org.jetbrains.skia.ImageInfo
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
 * 赛博朋克效果
 */
@ImageHandler
object CyberpunkHandler : FramesHandler {
    override val name = "赛博朋克"
    override val regex = Regex("(?i)赛博朋克|cyberpunk|sbpk|cbpk")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ) = frames.result {
        common(args).handle {
            val old = it.toBitmap()
            val new = Bitmap().also { bitmap ->
                bitmap.allocPixels(
                    ImageInfo(
                        width = it.imageInfo.width - 2,
                        height = it.imageInfo.height - 2,
                        colorType = it.imageInfo.colorType,
                        alphaType = it.imageInfo.colorAlphaType,
                        colorSpace = it.imageInfo.colorSpace
                    )
                )
            }
            for (x in 1 until old.width - 1) for (y in 1 until old.height - 1) {
                new.erase(old.cy(x, y), IRect.makeXYWH(x - 1, y - 1, 1, 1))
            }
            new.toImage()
        }
    }

    private fun Bitmap.cy(x: Int, y: Int): Int {
        val c = getColor(x, y)
        val a = c shr 24
        if (a == 0) return c
        val (pr, pg, pb) = getColor(x - 1, y - 1).rgb()
        val (nr, ng, nb) = getColor(x + 1, y + 1).rgb()
        val v = nr - pr + ng - pg + nb - pb
        return if (v < 0) argb(
            a,
            (-v / 5).limit(),
            (-v).limit(),
            (-v).limit(),
        ) else argb(
            a,
            (v).limit(),
            (v / 5).limit(),
            (v).limit(),
        )
    }
}
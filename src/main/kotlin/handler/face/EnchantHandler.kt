package top.e404.skiko.handler.face

import top.e404.skiko.apt.annotation.ImageHandler
import top.e404.skiko.frame.Frame
import top.e404.skiko.frame.FramesHandler
import top.e404.skiko.frame.HandleResult
import top.e404.skiko.frame.HandleResult.Companion.result
import top.e404.skiko.frame.common
import top.e404.skiko.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * 附魔特效
 */
@ImageHandler
object EnchantHandler : FramesHandler {
    private val bg by lazy { getJarImage("statistic/enchant.png") }

    override val name = "enchant"
    override val regex = Regex("附魔|(?i)enchant(ment)?")

    override suspend fun handleFrames(
        frames: MutableList<Frame>,
        args: MutableMap<String, String>,
    ): HandleResult {
        val count = if (frames.size != 1) frames.size
        else args["c"]?.toIntOrNull() ?: 40
        var i = 0
        val fs = (1..count).map {
            i++
            if (i >= frames.size) i = 0
            frames[i].clone()
        }.toMutableList()
        return fs.result {
            val image = frames.first().image
            val size = min(max(image.width, image.height), 400) * 3
            val resize = bg.resize(size, size)
            val unit = size / (count - 1)
            common(args).pmapIndexed { index ->
                limitAsGif(400F)
                also {
                    it.image = it.image.toSurface().withCanvas {
                        drawImage(this@pmapIndexed.image, 0F, 0F)
                        drawImage(resize, 0f,  unit * (count - 1 - index).toFloat())
                        drawImage(resize, 0f,  unit * (count - 1 - index).toFloat() - size)
                    }
                }
            }
        }
    }
}
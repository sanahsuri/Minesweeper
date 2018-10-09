package hu.ait.android.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.ait.android.minesweeper.MainActivity
import hu.ait.android.minesweeper.R
import hu.ait.android.minesweeper.model.FieldModel
import kotlinx.android.synthetic.main.activity_main.*


class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs)
{
    private val paintBackground: Paint = Paint()
    private val paintLine: Paint = Paint()

    private var bitmapMine = BitmapFactory.decodeResource(resources, R.drawable.mine)
    private var bitmapZero = BitmapFactory.decodeResource(resources, R.drawable.zero)
    private var bitmapOne = BitmapFactory.decodeResource(resources, R.drawable.one)
    private var bitmapTwo = BitmapFactory.decodeResource(resources, R.drawable.two)
    private var bitmapThree = BitmapFactory.decodeResource(resources, R.drawable.three)
    private var bitmapFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)


    var endGame: Boolean = false
    var flags: Boolean = false


    init {

        paintBackground.color = Color.GRAY;
        paintBackground.strokeWidth = 10F
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.strokeWidth = 10F
        paintLine.style = Paint.Style.STROKE

        FieldModel.putMines()
        (context as MainActivity).showFlags(FieldModel.flagCounter.toString())

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, width/5, height/5, false)
        bitmapOne = Bitmap.createScaledBitmap(bitmapOne, width/5, height/5, false)
        bitmapTwo = Bitmap.createScaledBitmap(bitmapTwo, width/5, height/5, false)
        bitmapThree = Bitmap.createScaledBitmap(bitmapThree, width/5, height/5, false)
        bitmapZero = Bitmap.createScaledBitmap(bitmapZero, width/5, height/5, false)
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, width/5, height/5, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(),
                paintBackground)
        drawGameArea(canvas)
        drawImages(canvas)
    }

    private fun drawGameArea(canvas: Canvas) {

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)

        for (i in 1..4) {
            canvas.drawLine(0f, (i * height / 5).toFloat(), width.toFloat(), (i * height / 5).toFloat(),
                    paintLine)
            canvas.drawLine((i * width / 5).toFloat(), 0f, (i * width / 5).toFloat(), height.toFloat(),
                    paintLine)
        }

    }

    private fun drawImages(canvas: Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                if (FieldModel.getFieldContent(i,j).wasClicked) {
                    if (FieldModel.getFieldContent(i,j).type == FieldModel.EMPTY) {
                        if (!FieldModel.getFieldContent(i, j).isFlagged) {
                            drawMinesAround(canvas, i, j)
                        }
                            else {
                            canvas.drawBitmap(bitmapFlag, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
                            drawMines(canvas)
                        }
                    } else {
                        if (!FieldModel.getFieldContent(i,j).isFlagged) {
                            drawMines(canvas)
                        } else {
                            canvas.drawBitmap(bitmapFlag, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
                        }
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val tX = event.x.toInt() / (width / 5)
        val tY = event.y.toInt() / (height / 5)
        if (!endGame) {
            if (tX < 5 && tY < 5) {
                FieldModel.getFieldContent(tX, tY).wasClicked = true
                (context as MainActivity).timer.start()
                if (flags) {
                    FieldModel.setFlag(tX, tY)
                }
                if (FieldModel.getFieldContent(tX, tY).type == FieldModel.EMPTY) {
                    onEmpty(tX, tY)
                }
                else if (FieldModel.getFieldContent(tX, tY).type == FieldModel.MINE){
                   onMine(tX, tY)
                }
            }
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    private fun onEmpty(tX : Int, tY : Int) {
        if (FieldModel.getFieldContent(tX, tY).isFlagged) {
            loseReset()
        } else {
            FieldModel.counter--
            if (FieldModel.onWin()) {
                winReset()
            }
        }
    }

    private fun onMine(tX : Int, tY : Int) {
        if (FieldModel.getFieldContent(tX, tY).isFlagged) {
            FieldModel.flagCounter--;
            (context as MainActivity).showFlags(FieldModel.flagCounter.toString())
            if (FieldModel.onWin()) {
                winReset()
            }
        } else {
            loseReset()
        }
    }

    private fun drawMines(canvas : Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                if (FieldModel.getFieldContent(i,j).type == FieldModel.MINE) {
                    canvas.drawBitmap(bitmapMine, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
                }
            }
        }
    }

    private fun drawMinesAround(canvas: Canvas, i : Int, j : Int) {
        when (FieldModel.getFieldContent(i,j).minesAround) {
            1->canvas.drawBitmap(bitmapOne, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
            2->canvas.drawBitmap(bitmapTwo, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
            3->canvas.drawBitmap(bitmapThree, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
            0->canvas.drawBitmap(bitmapZero, i * width.toFloat() / 5, j * height.toFloat() / 5, null)
        }
    }

    private fun winReset() {
        (context as MainActivity).showMessage(context.getString(R.string.win))
        (context as MainActivity).btnReset.setImageResource(R.drawable.happy)
        (context as MainActivity).timer.stop()
        endGame = true
        (context as MainActivity).gameOver()
    }

    private fun loseReset() {
        (context as MainActivity).showMessage(context.getString(R.string.lose))
        (context as MainActivity).btnReset.setImageResource(R.drawable.sad)
        (context as MainActivity).timer.stop()
        endGame = true
        (context as MainActivity).gameOver()
    }

    fun restart() {
        endGame = false
        FieldModel.resetModel()
        FieldModel.putMines()
        (context as MainActivity).showFlags(FieldModel.flagCounter.toString())
        (context as MainActivity).tvPlayer.text = ""
        (context as MainActivity).btnReset.setImageResource(R.drawable.smiley)
        (context as MainActivity).timer.base = SystemClock.elapsedRealtime()
        (context as MainActivity).timer.stop()
        invalidate()
    }

}
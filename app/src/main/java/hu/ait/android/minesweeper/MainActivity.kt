package hu.ait.android.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReset.setImageResource(R.drawable.smiley)

        btnReset.setOnClickListener {
            minesweeperview.restart()
        }

        tgFlag.setOnCheckedChangeListener { _, isChecked ->
            minesweeperview.flags = isChecked
        }

    }

    fun showMessage(msg: String) {
        tvPlayer.text = msg
    }

    fun showFlags(msg: String) {
        val toast = Toast.makeText(this, "You have $msg flag(s) left", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 600)
        toast.show()
    }

    fun gameOver() {
        Snackbar.make(minesweeperview, getString(R.string.over), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.reset)) {
            minesweeperview.restart()
        }.show()
    }

}

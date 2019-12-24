package posidon.launcher.feed.news.chooser

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import posidon.launcher.Main
import posidon.launcher.R
import posidon.launcher.tools.Settings
import posidon.launcher.tools.Tools

class FeedChooser : AppCompatActivity() {

    private lateinit var grid: RecyclerView
    private val feedUrls: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_chooser)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        grid = findViewById(R.id.grid)
        grid.layoutManager = GridLayoutManager(this, 2)
        val padding = (4 * resources.displayMetrics.density).toInt()
        grid.setPadding(padding, Tools.getStatusBarHeight(this), padding, Tools.navbarHeight + padding)

        feedUrls.addAll(Settings.getString("feedUrls", "androidpolice.com").split("|"))
        grid.adapter = FeedChooserAdapter(this@FeedChooser, feedUrls)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.backgroundTintList = ColorStateList.valueOf(Main.accentColor and 0x00ffffff or 0x33000000)
        fab.imageTintList = ColorStateList.valueOf(Main.accentColor)
        fab.setOnClickListener {
            Tools.vibrate(this)
            val dialog = BottomSheetDialog(this, R.style.bottomsheet)
            dialog.setContentView(R.layout.feed_chooser_option_edit_dialog)
            dialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(R.drawable.bottom_sheet)
            dialog.findViewById<TextView>(R.id.title)!!.backgroundTintList = ColorStateList.valueOf(Main.accentColor and 0x00ffffff or 0x33000000)
            dialog.findViewById<TextView>(R.id.done)!!.setTextColor(Main.accentColor)
            dialog.findViewById<TextView>(R.id.done)!!.backgroundTintList = ColorStateList.valueOf(Main.accentColor and 0x00ffffff or 0x33000000)
            dialog.findViewById<TextView>(R.id.done)!!.setOnClickListener {
                dialog.dismiss()
                feedUrls.add(dialog.findViewById<EditText>(R.id.title)!!.text.toString().replace('|', ' '))
                grid.adapter!!.notifyDataSetChanged()
                Settings.putString("feedUrls", feedUrls.joinToString("|"))
            }
            dialog.findViewById<TextView>(R.id.remove)!!.visibility = View.GONE
            dialog.show()
        }
    }

    override fun onPause() {
        super.onPause()
        feedUrls.clear()
    }
}
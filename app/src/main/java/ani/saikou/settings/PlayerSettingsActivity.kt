package ani.saikou.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import ani.saikou.*
import ani.saikou.databinding.ActivityPlayerSettingsBinding


class PlayerSettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivityPlayerSettingsBinding
    private val player = "player_settings"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivity(this)
        binding.playerSettingsContainer.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = statusBarHeight
            bottomMargin = navBarHeight
        }

        val settings = loadData<PlayerSettings>(player)?: PlayerSettings()

        //Video
        binding.playerSettingsVideoInfo.isChecked = settings.videoInfo
        binding.playerSettingsVideoInfo.setOnCheckedChangeListener { _, isChecked ->
            settings.videoInfo = isChecked
            saveData(player,settings)
        }

        binding.playerSettingsQualityHeight.setText((loadData<Int>("maxHeight")?:480).toString())
        binding.playerSettingsQualityHeight.addTextChangedListener {
            val height = binding.playerSettingsQualityHeight.text.toString().toIntOrNull()?:return@addTextChangedListener
            saveData("maxHeight",height)
        }
        binding.playerSettingsQualityWidth.setText((loadData<Int>("maxWidth")?:720).toString())
        binding.playerSettingsQualityWidth.addTextChangedListener {
            val height = binding.playerSettingsQualityWidth.text.toString().toIntOrNull()?:return@addTextChangedListener
            saveData("maxWidth",height)
        }


        val speeds     = arrayOf( 0.25f , 0.33f , 0.5f , 0.66f , 0.75f , 1f , 1.25f , 1.33f , 1.5f , 1.66f , 1.75f , 2f )
        val speedsName = speeds.map { "${it}x" }.toTypedArray()
        binding.playerSettingsSpeed.text = getString(R.string.default_playback_speed,speedsName[settings.defaultSpeed])
        val speedDialog = AlertDialog.Builder(this,R.style.DialogTheme).setTitle("Default Speed")
        binding.playerSettingsSpeed.setOnClickListener{
            speedDialog.setSingleChoiceItems(speedsName,settings.defaultSpeed) { dialog, i ->
                settings.defaultSpeed = i
                binding.playerSettingsSpeed.text = getString(R.string.default_playback_speed,speedsName[i])
                saveData(player,settings)
                dialog.dismiss()
            }.show()
        }

        //Behaviour

        binding.playerSettingsPauseVideo.isChecked = settings.focusPause
        binding.playerSettingsPauseVideo.setOnCheckedChangeListener { _, isChecked ->
            settings.focusPause = isChecked
            saveData(player,settings)
        }
        binding.playerSettingsVerticalGestures.isChecked = settings.gestures
        binding.playerSettingsVerticalGestures.setOnCheckedChangeListener { _, isChecked ->
            settings.gestures = isChecked
            saveData(player,settings)
        }

        binding.playerSettingsDoubleTap.isChecked = settings.doubleTap
        binding.playerSettingsDoubleTap.setOnCheckedChangeListener { _, isChecked ->
            settings.doubleTap = isChecked
            saveData(player,settings)
        }

        binding.playerSettingsSeekTime.value = settings.seekTime.toFloat()
        binding.playerSettingsSeekTime.addOnChangeListener { _, value, _ ->
            settings.seekTime = value.toInt()
            saveData(player,settings)
        }

        binding.exoSkipTime.setText(settings.skipTime.toString())
        binding.exoSkipTime.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.exoSkipTime.clearFocus()
            }
            false
        }
        binding.exoSkipTime.addTextChangedListener {
            val time = binding.exoSkipTime.text.toString().toIntOrNull()
            if(time!=null) {
                settings.skipTime = time
                saveData(player,settings)
            }
        }

        binding.playerSettingsCast.isChecked = settings.cast
        binding.playerSettingsCast.setOnCheckedChangeListener { _, isChecked ->
            settings.cast = isChecked
            saveData(player,settings)
        }


    }
}
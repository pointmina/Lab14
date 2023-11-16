package com.example.ch14_receiver

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.lab14.R
import com.example.lab14.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 권한 요청 후 브로드캐스트 실행
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            } else {
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 동적으로 레지스트 리시버 등록
        registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )?.apply {
            when (getIntArrayExtra(BatteryManager.EXTRA_PLUGGED)?.get(0)) {
                BatteryManager.BATTERY_PLUGGED_USB -> {
                    binding.chargingResultView.text = "USB 충전 중"
                    binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.usb))
                }
                BatteryManager.BATTERY_PLUGGED_AC -> {
                    binding.chargingResultView.text = "고속 충전 중"
                    binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ac))
                }
                else -> {
                    binding.chargingResultView.text = "충전선을 연결하세요"
                }
            }

            // 남은 배터리 잔량 표시
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            binding.percentResultView.text = "$batteryPct %"
        }

        // 버튼 클릭 시 브로드캐스트 실행
        binding.button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this, "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    val intent = Intent(this, MyReceiver::class.java)
                    sendBroadcast(intent)
                } else {
                    permissionLauncher.launch(
                        arrayOf("android.permission.POST_NOTIFICATIONS")
                    )
                }
            } else {
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }
        }
    }
}

package com.minoflower.electronicalbum

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private val addPhotoButton: AppCompatButton by lazy {
        findViewById(R.id.addPhotoButton)
    }

    private val startButton: AppCompatButton by lazy {
        findViewById(R.id.startPhotoFrameModeButton)
    }

    private val imageList: List<ImageView> by lazy {
        listOf(
            findViewById(R.id.image11),
            findViewById(R.id.image12),
            findViewById(R.id.image13),
            findViewById(R.id.image21),
            findViewById(R.id.image22),
            findViewById(R.id.image23)
        )
    }

    private val imageUriList: MutableList<Uri> = mutableListOf()

    private val getContent =
        run {
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    if (imageUriList.size == MAX_IMAGE_LIST_SIZE) {
                        Toast.makeText(this, "사진이 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }

                    imageUriList.add(uri)
                    imageList[imageUriList.size - 1].setImageURI(uri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAddButton()
        initStartButton()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initAddButton() {
        addPhotoButton.setOnClickListener {
            when {
                // 퍼미션 허가가 돼있는지 확인. 돼있다면 실행
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navigatePhotos()
                }

                //권한이 거부된 상태일 때 사용자에게 권한을 설정할 것인지 물어봄
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionPopup()
                }

                //퍼미션 허가가 안된 경우
                else -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                }
            }
        }
    }

    private fun initStartButton() {
        startButton.setOnClickListener {
            if (imageUriList.isEmpty()) {
                Toast.makeText(this, "사진이 존재하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PhotoFrameActivity::class.java)
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("photo$index", uri.toString())
            }
            intent.putExtra("listSize", imageUriList.size)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한이 거부된 상태입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한을 설정하세요")
            .setMessage("앱에서 사진을 불러오기 위해 권한을 허가해주세요")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
            .setNegativeButton("닫기") { _, _ -> }
            .create()
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun navigatePhotos() {
        getContent.launch("image/*")
    }


    companion object {
        private const val REQUEST_CODE = 1000
        private const val MAX_IMAGE_LIST_SIZE = 6
    }
}
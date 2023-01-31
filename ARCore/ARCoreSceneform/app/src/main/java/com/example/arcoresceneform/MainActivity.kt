package com.example.arcoresceneform

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val MIN_OPENGL_VERSION = 3.0

class MainActivity : AppCompatActivity() {
    private var arFragment: ArFragment? = null
    private lateinit var renderable: ModelRenderable

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFirebase()
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?
        findViewById<Button>(R.id.btn_capture).setOnClickListener {
            getBitmapFromView(findViewById(R.id.arFragment)){
                screenShot(it)
            }
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
        val storage = FirebaseStorage.getInstance()
        // 파이어베이스 저장소에서 파일명으로 가져옴
        val modelRef = storage.reference.child("sofa.glb") as StorageReference
        // sofa.glb라는 빈파일 생성
        val file = File.createTempFile("sofa", "glb")
        // StorageReference에서 file에 파일을 비동기식으로 다운로드.
        writeFileFromFirebase(modelRef, file)

    }

    private fun writeFileFromFirebase(modelRef: StorageReference, file: File) {
        modelRef.getFile(file)
            .addOnSuccessListener { // 다운로드 성공
                buildModel(file)
            }.addOnFailureListener { // 다운로드 실패
                Toast.makeText(this, "Failed to download", Toast.LENGTH_SHORT).show()
            }
    }

    /**매개변수로 주어진 file을 3D 모델로 빌드*/
    private fun buildModel(file: File) {

        val renderableSource = RenderableSource.builder()
            .setSource(this, Uri.parse(file.path), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT).build()

        ModelRenderable.builder()
            .setSource(this, renderableSource)
            .setRegistryId(file.path).build()
            .thenAccept { modelRenderable ->
                Toast.makeText(this, "Model built!", Toast.LENGTH_SHORT).show()
                renderable = modelRenderable

                // ARCore 평면을 탭하면 호출됩니다.
                arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
                    val anchor = hitResult.createAnchor()
                    addModelToScene(anchor, renderable)
                }
            }.exceptionally {
                val toast = Toast.makeText(
                    this, "Unable to load renderable: " + file.path, Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }
    }

    private fun addModelToScene(anchor: Anchor, modelRenderable: ModelRenderable) {

        // ARCore 앵커를 기반으로 월드 공간에 자동으로 배치되는 노드
        val node = AnchorNode(anchor)

        // TransformationSystem의 동작을 사용하여 선택하고 변환하고 회전하고 확장할 수 있는 노드
        val transformableNode = TransformableNode(arFragment!!.transformationSystem)
        transformableNode.setParent(node)
        transformableNode.scaleController.maxScale = 1.0f
        transformableNode.scaleController.minScale = 0.8f
        transformableNode.renderable = modelRenderable

        // 현재 AR Scene에 노드를 등록
        arFragment!!.arSceneView.scene.addChild(node)
        // 설치된 노드를 선택상태로 만듬
        transformableNode.select()
    }

    fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e("suyong", "Sceneform requires Android N or later")
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        val openGlVersionString =
            (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Log.e("suyong", "Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        return true
    }

    @RequiresApi(VERSION_CODES.O)
    fun getBitmapFromView(view: View, callback: (Bitmap?) -> Unit) {
        Log.d("suyong", "starting capture")
        // 비트맵 생성
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        // 뷰의 좌표를 계산하여 배열에 x, y가 반환
        val locationOfViewInWindow = IntArray(2)
        view.getLocationInWindow(locationOfViewInWindow)
        try {
            PixelCopy.request(
                arFragment!!.arSceneView,
                Rect(
                    locationOfViewInWindow[0],
                    locationOfViewInWindow[1],
                    locationOfViewInWindow[0] + view.width,
                    locationOfViewInWindow[1] + view.height
                ),
                bitmap,
                { copyResult -> // PixelCopy.OnPixelCopyFinishedListener
                    if (copyResult == PixelCopy.SUCCESS) callback.invoke(bitmap)
                    else callback.invoke(null)
                }, Handler(Looper.getMainLooper())
            )
        } catch (e: java.lang.IllegalArgumentException) {
            callback.invoke(null)
        }
    }

    private fun screenShot(bitmap: Bitmap?) {
        try {
            Log.d("suyong", "sharing picture")

            /* 캐시 디렉터리에서 images*/
            val cachePath = File(cacheDir, "images")
            Log.d("suyong", "${cachePath.path}")

            /* cache/images 디렉터리가 없으면 생성 */
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.png")

            /* bitmap -> png */
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            /* cache/images/image.png */
            val newFile = File(cachePath, "image.png")
            val contentUri =
                FileProvider.getUriForFile(this, "com.example.arcoresceneform", newFile)

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "image/png"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(sharingIntent, "share image"))
        } catch (e: IOException) {
            Log.d("suyong", "faild to sharing")
            e.printStackTrace()
        }
    }
}
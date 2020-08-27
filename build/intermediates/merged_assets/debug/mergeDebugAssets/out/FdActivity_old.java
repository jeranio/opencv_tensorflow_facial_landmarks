//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//import java.nio.MappedByteBuffer;
//import java.util.Collections;
//import java.util.Hashtable;
//import java.util.List;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraActivity;
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfRect;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
//import org.opencv.objdetect.CascadeClassifier;
//import org.opencv.imgproc.Imgproc;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.WindowManager;
//
//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.Interpreter;
//import org.tensorflow.lite.support.common.FileUtil;
//import org.tensorflow.lite.support.common.TensorOperator;
//import org.tensorflow.lite.support.common.TensorProcessor;
//import org.tensorflow.lite.support.common.ops.NormalizeOp;
//import org.tensorflow.lite.support.image.ImageProcessor;
//import org.tensorflow.lite.support.image.TensorImage;
//import org.tensorflow.lite.support.image.ops.ResizeOp;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
//import org.tensorflow.lite.support.model.Model;
//
//
//public class FdActivity extends CameraActivity implements CvCameraViewListener2 {
//
//    private static final String    TAG                 = "OCVSample::Activity";
//    private static final Scalar    FACE_RECT_COLOR     = new Scalar(255, 255, 0, 255);
//    public static final int        JAVA_DETECTOR       = 0;
//    public static final int        NATIVE_DETECTOR     = 1;
//
//    private MenuItem               mItemFace50;
//    private MenuItem               mItemFace40;
//    private MenuItem               mItemFace30;
//    private MenuItem               mItemFace20;
//    private MenuItem               mItemType;
//
//    private Mat                    mRgba;
//    private Mat                    mGray;
//    private File                   mCascadeFile;
//    private CascadeClassifier      mJavaDetector;
//    private DetectionBasedTracker  mNativeDetector;
//
//    //private int                    mDetectorType       = JAVA_DETECTOR;
//    private int                    mDetectorType       = NATIVE_DETECTOR;
//    private String[]               mDetectorName;
//
//    private float                  mRelativeFaceSize   = 0.2f;
//    private int                    mAbsoluteFaceSize   = 0;
//
//    private CameraBridgeViewBase   mOpenCvCameraView;
//
//    private static final float IMAGE_MEAN = 0.0f;
//    private static final float IMAGE_STD = 1.0f;
//    /** Quantized MobileNet requires additional dequantization to the output probability. */
//    private static final float PROBABILITY_MEAN = 0.0f;
//    private static final float PROBABILITY_STD = 255.0f;
//
//    private Bitmap myBitMap;
//    private Interpreter tflite;
//
//    ImageProcessor imageProcessor =
////            new ImageProcessor.Builder()
////                    .add(new ResizeOp(64, 64, ResizeOp.ResizeMethod.BILINEAR))
////                    .build();
//                new ImageProcessor.Builder()
//                        .add(new ResizeOp(64, 64, ResizeOp.ResizeMethod.BILINEAR))
//                        .build();
//
//    TensorImage tImage = new TensorImage(DataType.FLOAT32);
//    TensorBuffer probabilityBuffer = TensorBuffer.createFixedSize(new int[]{1,136}, DataType.FLOAT32);
//
//
//    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS:
//                {
//                    Log.i(TAG, "OpenCV loaded successfully");
//
//                    // Load native library after(!) OpenCV initialization
//                    System.loadLibrary("detection_based_tracker");
//
//                    // Initialise the model assets/facial_landmark_MobileNet.tflite
//                    try{
//                        MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(getApplicationContext(), "facial_landmark_MobileNet.tflite");
//                        tflite = new Interpreter(tfliteModel);
//                        if (null != tflite) {
//                            Log.e("tfliteSupport >>>", "model loaded");
//
//                        }
//                    } catch (IOException e){
//                        Log.e("tfliteSupport >>>", "Error reading model", e);
//                    }
//
//                    try {
//                        // load cascade file from application resources
//                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
//                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
//                        FileOutputStream os = new FileOutputStream(mCascadeFile);
//
//                        byte[] buffer = new byte[4096];
//                        int bytesRead;
//                        while ((bytesRead = is.read(buffer)) != -1) {
//                            os.write(buffer, 0, bytesRead);
//                        }
//                        is.close();
//                        os.close();
//
//                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
//                        if (mJavaDetector.empty()) {
//                            Log.e(TAG, "Failed to load cascade classifier");
//                            mJavaDetector = null;
//                        } else
//                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
//
//                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);
//
//                        cascadeDir.delete();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
//                    }
//
//                    mOpenCvCameraView.enableView();
//                } break;
//                default:
//                {
//                    super.onManagerConnected(status);
//                } break;
//            }
//        }
//    };
//
//    public FdActivity() {
//        mDetectorName = new String[2];
//        mDetectorName[JAVA_DETECTOR] = "Java";
//        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";
//        Log.i(TAG, "Instantiated new " + this.getClass());
//    }
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.i(TAG, "called onCreate");
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        setContentView(R.layout.face_detect_surface_view);
//        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
//        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
//    }
//
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        if (!OpenCVLoader.initDebug()) {
//            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
//        } else {
//            Log.d(TAG, "OpenCV library found inside package. Using it!");
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }
//    }
//
//    @Override
//    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
//        return Collections.singletonList(mOpenCvCameraView);
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        mOpenCvCameraView.disableView();
//    }
//
//    public void onCameraViewStarted(int width, int height) {
//        mGray = new Mat();
//        mRgba = new Mat();
//    }
//
//    public void onCameraViewStopped() {
//        mGray.release();
//        mRgba.release();
//    }
//
//    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
//
//        mRgba = inputFrame.rgba();
//        mGray = inputFrame.gray();
//
//        if (mAbsoluteFaceSize == 0) {
//            int height = mGray.rows();
//            if (Math.round(height * mRelativeFaceSize) > 0) {
//                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
//            }
//            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
//        }
//
//        MatOfRect faces = new MatOfRect();
//
//        if (mDetectorType == JAVA_DETECTOR) {
//            if (mJavaDetector != null)
//                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
//                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
//        }
//        else if (mDetectorType == NATIVE_DETECTOR) {
//            if (mNativeDetector != null)
//                mNativeDetector.detect(mGray, faces);
//        }
//        else {
//            Log.e(TAG, "Detection method is not selected!");
//        }
//
//        Rect[] facesArray = faces.toArray();
//        Log.d(">>>> faces ",facesArray.toString());
//
//        for (int i = 0; i < facesArray.length; i++) {
//            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 2);
//            // tensforflow stuff
//            Rect roi = new Rect(facesArray[i].x,facesArray[i].y,facesArray[i].width,facesArray[i].height );
//            Mat myMat = new Mat(mGray,roi);
//            //Bitmap.Config config;
//            myBitMap = Bitmap.createBitmap(myMat.width(), myMat.height(), Config.ARGB_8888);
//
//            Bitmap bmpGray = toGrayscale(myBitMap);
//
//            //Mat myMatResized = new Mat();
//            //Imgproc.resize(myMat,myMatResized,new Size(64,64));
//
//            Log.i(TAG, "T-f-l: >>>" + tflite);
//
//            if (null != bmpGray) {
//                tImage.load(bmpGray);
//                tImage = imageProcessor.process(tImage);
//                Log.e(TAG, "onCameraFrame >>>: " + tImage );
//                tflite.run(tImage.getBuffer(),probabilityBuffer.getBuffer());
//                //TensorProcessor probabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD)).build();
//                TensorProcessor probabilityProcessor = new TensorProcessor.Builder().build();
//                Log.i(TAG, "landmark_data >>>: " + probabilityProcessor);
//            }
//        }
//        return mRgba;
//    }
//
//
//    public Bitmap toGrayscale(Bitmap bmpOriginal)
//    {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmpGrayscale);
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//        paint.setColorFilter(f);
//        c.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.i(TAG, "called onCreateOptionsMenu");
//        mItemFace50 = menu.add("Face size 50%");
//        mItemFace40 = menu.add("Face size 40%");
//        mItemFace30 = menu.add("Face size 30%");
//        mItemFace20 = menu.add("Face size 20%");
//        mItemType   = menu.add(mDetectorName[mDetectorType]);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
//        if (item == mItemFace50)
//            setMinFaceSize(0.5f);
//        else if (item == mItemFace40)
//            setMinFaceSize(0.4f);
//        else if (item == mItemFace30)
//            setMinFaceSize(0.3f);
//        else if (item == mItemFace20)
//            setMinFaceSize(0.2f);
//        else if (item == mItemType) {
//            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
//            item.setTitle(mDetectorName[tmpDetectorType]);
//            setDetectorType(tmpDetectorType);
//        }
//        return true;
//    }
//
//    private void setMinFaceSize(float faceSize) {
//        mRelativeFaceSize = faceSize;
//        mAbsoluteFaceSize = 0;
//    }
//
//    private void setDetectorType(int type) {
//        if (mDetectorType != type) {
//            mDetectorType = type;
//
//            if (type == NATIVE_DETECTOR) {
//                Log.i(TAG, "Detection Based Tracker enabled");
//                mNativeDetector.start();
//            } else {
//                Log.i(TAG, "Cascade detector enabled");
//                mNativeDetector.stop();
//            }
//        }
//    }
//}

package com.example.curameapplication;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.DequantizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.tensorflow.lite.support.common.FileUtil.loadMappedFile;

public class ImageClassifier {

    //Image file path
    String imageFilePath;

    Bitmap imageBitmap;

    //Image size needed by the model
    private final int ySize, xSize;

    private Interpreter model;

    //Interpreter options
    private Interpreter.Options options;

    //Tflite model
    private MappedByteBuffer loadedModel;

    //Model and label paths
    private String modelPath, labelPath;

    //Stores labels
    private List<String> labels;

    //Tensor input shape
    int[] modelInputShape = {1, 224, 224, 3};

    //Tensor output shape
    int[] modelOutputShape = {1, 7}; //7 Classes

    //Input & Output tensor
    private TensorImage inputBuffer;
    private TensorBuffer outputBuffer;

    //Dequantize the output results
    private TensorProcessor probability;

    //Converts probabilities from floating point notation
    TensorProcessor fpProbabilityConv;

    //Data the model inputs
    DataType datatype;

    public ImageClassifier(Activity activity, String imageFilePath) {
        modelPath = "model.tflite";
        labelPath = "labels.txt";
        //"/sdcard/Download/ISIC_0024898.jpg"
        this.imageFilePath = imageFilePath;

        //Fetch image bitmap from URI
        try {
            imageBitmap = BitmapFactory.decodeFile(imageFilePath);
        } catch (Exception e) {
            Log.d("BITMAP ERROR", "ImageClassifier: " + e.getMessage());
        }

        //Image Size
        ySize = 224;
        xSize = 224;

        //Load model from assets folder
        try {
            loadedModel = loadMappedFile(activity, modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Interpreter.Options instance
        options = new Interpreter.Options();

        //Add number of threads to use to the interpreter options
        options.setNumThreads(5);

        //Model with options
        model = new Interpreter(loadedModel, options);

        //Load labels from assets folder
        try {
            labels = FileUtil.loadLabels(activity, labelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Grab data type for the tflite model (UINT16)
        int outputIndex = 0;
        datatype = model.getOutputTensor(outputIndex).dataType();


        //Input tensor
        inputBuffer = new TensorImage(datatype);

        //Output tensor
        outputBuffer = TensorBuffer.createFixedSize(modelOutputShape, datatype);

        //Dequantize the output results (converts from floating point numbers)
        fpProbabilityConv = new TensorProcessor.Builder().add(new DequantizeOp(0, (float) (1/255.0))).build();

    }

    private TensorImage ImagePreprocessor(Bitmap imageBitmap) {

        //Creates new tensor image
        TensorImage image = new TensorImage(datatype);

        //stores the imageBitmap in the tensor image variable
        image.load(imageBitmap);

        //preprocessor function to resize the tensor
        ImageProcessor preprocessor = new ImageProcessor.Builder().add(new ResizeOp(xSize, ySize, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR)).build();

        return preprocessor.process(image);
    }

    //public List<String> Classify() {
    public Map<String, Float> Classify() {
        TensorImage tensorImage = ImagePreprocessor(imageBitmap);

        model.run(tensorImage.getBuffer(), outputBuffer.getBuffer());

        Log.d("OVER HERE", "Classify: Model initialized");
        TensorLabel outputProb = new TensorLabel(labels, fpProbabilityConv.process(outputBuffer));

        Map<String, Float> floatMap = outputProb.getMapWithFloatValue();
        return floatMap;
    }
}

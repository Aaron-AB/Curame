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
import java.util.TreeMap;

import static org.tensorflow.lite.support.common.FileUtil.loadMappedFile;

public class ImageClassifier {

    //Image file path
    String imageFilePath;

    Bitmap imageBitmap;

    /**
     * Image size needed by the model
     */
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
        fpProbabilityConv = new TensorProcessor.Builder().add(new DequantizeOp(0, (float) (1.0))).build();

    }

    /**
     * Load image into a bitmap
     * Store bitmap in a tensor
     * Resize tensor to match the model's input size
     * @param imageBitmap
     * @return
     */
    public TensorImage ImagePreprocessor(Bitmap imageBitmap) {

        //Creates new tensor image
        TensorImage image = new TensorImage(datatype);

        //stores the imageBitmap in the tensor image variable
        image.load(imageBitmap);

        //preprocessor function to resize the tensor
        ImageProcessor preprocessor = new ImageProcessor.Builder().add(new ResizeOp(xSize, ySize, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR)).build();

        return preprocessor.process(image);
    }

    /**
     * Runs an inference on the model,
     * returns a Map of the highest values
     * @return
     */
    public Map<String, Float> Classify() {
        TensorImage tensorImage = ImagePreprocessor(imageBitmap);

        model.run(tensorImage.getBuffer(), outputBuffer.getBuffer());

        Log.d("OVER HERE", "Classify: Model initialized");
        TensorLabel outputProb = new TensorLabel(labels, fpProbabilityConv.process(outputBuffer));

        Map<String, Float> floatMap = outputProb.getMapWithFloatValue();

        /**
         * Prune any low values
         */
        floatMap = highestClassification(floatMap);

        return floatMap;
    }

    /**
     * Accepts a floatMap as an input,
     * Finds the highest value,
     * Also finds the next highest values that are relevant,
     * Outliers are not included
     * @param floatMap
     * @return
     */
    public Map<String, Float> highestClassification(Map<String, Float> floatMap){
        String highestKey = "";
        Float highestValue = new Float(0.0);

        Map<String, Float> highestMap = new TreeMap<String, Float>();

        //Get the highest values
        for (String key : floatMap.keySet()){
            if (floatMap.get(key) > highestValue){
                highestKey = key;
                highestValue = floatMap.get(key);
            }
        }

        //Add the highest to the map
        highestMap.put(highestKey, floatMap.remove(highestKey));

        boolean found;
        String nextKey;
        Float nextValue;

        //Now find values that are near the highest
        do{
            found = false;
            nextKey = "";
            nextValue = new Float(0.0);

            //Find the next highest in the map
            for (String key : floatMap.keySet()){
                if (floatMap.get(key) > nextValue){
                    nextKey = key;
                    nextValue = floatMap.get(key);
                }
            }

            Log.d("VALUE FOUND", Float.toString(highestValue - nextValue));
            //if the next highest is 10% or less than the highest, add it to the map or if the value is above 20%
            if(((highestValue - nextValue) <= new Float(0.10)) || (nextValue>new Float(0.2))){
                found = true;
                highestMap.put(nextKey, floatMap.remove(nextKey));
            }
        }while(found);

        //return the highest map
        return highestMap;
    }
}

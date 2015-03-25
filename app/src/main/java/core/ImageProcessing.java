package core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * This class is uesd to carry out operations related to the processing of the profile picture
 * Created on 11/02/2015.
 * @author Loucas Stylianou
 */
public class ImageProcessing {
    private ImageProcessing(){}

    /**
     * Encode the image in order to send it to the server to store it
     * @param bp A bitmap object
     * @return Encoded string
     */
    public static String encodeImage(Bitmap bp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return image_str;
    }

    /**
     * Decode the profile picture from a give string image stream.
     * @param imgStream This is usually sent from the server
     * @return A bitmap object which can be used to display the profile picture to the user
     */
    public static Bitmap decodeImage(String imgStream){
        byte[] byte_arr = Base64.decode(imgStream,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
/*        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));*/
        return bitmap;
    }
}

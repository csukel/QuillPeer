package core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by loucas on 11/02/2015.
 */
public class ImageProcessing {
    private ImageProcessing(){}
    public static String encodeImage(Bitmap bp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return image_str;
    }

    public static Bitmap decodeImage(String imgStream){
        byte[] byte_arr = Base64.decode(imgStream,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
/*        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));*/
        return bitmap;
    }
}

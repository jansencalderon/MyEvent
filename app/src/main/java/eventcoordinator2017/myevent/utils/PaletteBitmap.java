package eventcoordinator2017.myevent.utils;

/**
 * Created by Mark Jansen Calderon on 2/10/2017.
 */


import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

/**
 * A simple wrapper for a {@link Palette} and a {@link android.graphics.Bitmap}.
 */
public class PaletteBitmap {
    public final Palette palette;
    public final Bitmap bitmap;

    public PaletteBitmap(@NonNull Bitmap bitmap, Palette palette) {
        this.bitmap = bitmap;
        this.palette = palette;
    }
}

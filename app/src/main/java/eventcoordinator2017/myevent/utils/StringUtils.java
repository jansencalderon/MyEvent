package eventcoordinator2017.myevent.utils;

import java.text.NumberFormat;

/**
 * Created by Mark Jansen Calderon on 2/2/2017.
 */

public class StringUtils {

    public static String toCurrency(int s){
        NumberFormat format = NumberFormat.getCurrencyInstance();

        return format.format(s);
    }
}

package com.zhx.zxlibrary.commUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    /**
     * 保留两位小数
     *
     * @param row
     * @return
     */
    public static String getdoubleToString(double row) {
        DecimalFormat df = new DecimalFormat("######0.00");
        double d = getdoubletwo(row);
        String st = df.format(d);
        return st;
    }

    public static double getdoubletwo(double row) {
        DecimalFormat r = new DecimalFormat();
        r.applyPattern("##0.00");
        return new Double(r.format(row)).doubleValue();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }


    // 时间格式
    public static String getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = df.format(new Date());
        return currentDateTime;
    }

    // 时间格式
    public static String getCurrentDateTime2() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateTime = df.format(new Date());
        return currentDateTime;
    } // 时间格式

    public static String getCurrentDateTime(String format) {
        DateFormat df = new SimpleDateFormat(format);
        String currentDateTime = df.format(new Date());
        return currentDateTime;
    }

    /**
     * 网络图片Url 转 Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapHttp(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date1(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 是否开始  开始为1  结束为2  其他则为剩余时间
     * 如果倒计时精确到毫秒  则返回时*1000
     *
     * @param start_time
     * @param end_time
     * @return
     */
    public static long gettimeLong(String start_time, String end_time) {

        Log.e("start_time", start_time + "end_time:" + end_time);

        String system_time = CommonUtil.getCurrentDateTime();
        String[] system_times = system_time.split(" ");
        String str_end = system_times[0] + " " + end_time;
        String str_start = system_times[0] + " " + start_time;
        long long_end = CommonUtil.stringToLong(str_end, "yyyy-MM-dd HH:mm:ss");
        long long_start_time = CommonUtil.stringToLong(str_start, "yyyy-MM-dd HH:mm:ss");
        long long_system_time = CommonUtil.stringToLong(system_time, "yyyy-MM-dd HH:mm:ss");
        if (long_system_time < long_start_time) {
            return 1;
        }
        if (long_system_time > long_end) {
            return 2;
        }
        return (long_end - long_system_time);
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, formatType);

            if (date == null) {
                return 0;
            } else {
                long currentTime = dateToLong(date); // date类型转成long类型
                return currentTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * @param end_time
     * @return
     */
    public static String gettimeList(String end_time) {
        String system_time = CommonUtil.getCurrentDateTime();
        String[] system_times = system_time.split(" ");
        String str_end = system_times[0] + " " + end_time;
        return str_end;
    }

    /**
     * 获取结束时间的long
     *
     * @param end_time
     * @return
     */
    public static long gettimeend(String end_time) {
        end_time = gettimeList(end_time);
        long now = System.currentTimeMillis();
        try {
            long s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(end_time).getTime();// 根据字符串time得到毫秒数。
            long data = System.currentTimeMillis() + (s - now);
            return data;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    // String 转data

    public static Long getCurrentDateTime2data(String data) {
        long lTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dt2 = null;

            dt2 = sdf.parse(data);

            // 继续转换得到秒数的long型
            lTime = dt2.getTime() / 1000;


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lTime;

    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    // dip转化成px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * bitmap转换成String
     */
    public static String ImageToBytes(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] buffer = out.toByteArray();
        String photo = Base64.encodeToString(buffer, Base64.DEFAULT);
        return photo;
    }

    /**
     */
    public static String encodeBase64File_picture(String path) {
        String data = "";
        Bitmap bitmap = null;
        try {

            bitmap = getimage(path);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            byte[] buffer = out.toByteArray();

            data = Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，高和宽我们设置为
        float hh = 800f;// 这里设置高度
        float ww = 480f;// 这里设置宽度
        // 缩放比由于是固定比例缩放，只用高或者宽其中数据进行计算即可
        int be = 1;// be=1表示不缩
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这00表示不压缩，把压缩后的数据存放到baos
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大00kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos
            options -= 10;// 每次都减
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static boolean isGoogleMapsInstalled(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public static String encodeBase64File_picture_bitmap(Bitmap bitMapsrc) {
        String data = "";
        Bitmap bitmap = null;
        try {

            bitmap = bitMapsrc;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            byte[] buffer = out.toByteArray();

            data = Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    /**
     * 中国正常坐标系GCJ02协议的坐标，转到 百度地图对应的 BD09 协议坐标
     *
     * @param lat
     * @param lng
     */
    public static void Convert_GCJ02_To_BD09(double lat, double lng) {
        double x = lng, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta) + 0.0065;
        lat = z * Math.sin(theta) + 0.006;
    }

    public static double Convert_GCJ02_To_BD09_Lat(double lat, double lng) {
        double x = lng, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta) + 0.0065;
        lat = z * Math.sin(theta) + 0.006;
        return lat;
    }

    public static double Convert_GCJ02_To_BD09_Lng(double lat, double lng) {
        double x = lng, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta) + 0.0065;
        lat = z * Math.sin(theta) + 0.006;
        return lng;
    }

    /**
     * 百度地图对应的 BD09 协议坐标，转到 中国正常坐标系GCJ02协议的坐标
     *
     * @param lat
     * @param lng
     */
    public static void Convert_BD09_To_GCJ02(double lat, double lng) {
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta);
        lat = z * Math.sin(theta);
    }

    public static double Convert_BD09_To_GCJ02_Lat(double lat, double lng) {
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta);
        lat = z * Math.sin(theta);
        return lat;
    }

    public static double Convert_BD09_To_GCJ02_Lng(double lat, double lng) {
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta);
        lat = z * Math.sin(theta);
        return lng;
    }

    public static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long currentTime = System.currentTimeMillis();
        long timeD = currentTime - lastClickTime;
        if (0 < timeD && timeD < 2000) {
            return false;
        }
        lastClickTime = currentTime;
        return true;
    }


    public static Bitmap getnetBitmap(String s) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(s);
            bitmap = BitmapFactory.decodeStream(url.openStream());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Drawable zoomBitmaptoDrawable(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap oldbmp = bitmap;
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static long getBitmapsize(Bitmap bitmap) {

		/*
         * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
		 * return bitmap.getByteCount(); }
		 */
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    public static long geturisize(Context context, Uri uri) {

        String img_path = getRealPathFromURI(context, uri);

        File file = new File(img_path);

        return file.length();
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj,
                null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL myurl = new URL(url);
            conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            bmp = BitmapFactory.decodeStream(is, null, options);
            is.close();
            // byte[] data = readStream(is);
            // bmp = BitmapFactory.decodeByteArray(data, 0, data.length,
            // options);
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bmp;
    }

    public static Bitmap getURLimage_2(String url) {
        Bitmap bmp = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL myurl = new URL(url);
            conn = (HttpURLConnection) myurl.openConnection();
            conn.connect();
            is = conn.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();

            bmp = BitmapFactory.decodeStream(is, null, options);

            is.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bmp;
    }

    public static Bitmap getUrlBitmap(String data) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(data);
            bitmap = BitmapFactory.decodeStream(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String analysisStringBuffer_firstsymbol_after(
            StringBuffer strBuffer, String symbol) {
        String str = "";
        if (!isEmpty(strBuffer.toString())) {
            str = strBuffer.toString().substring(
                    strBuffer.toString().indexOf(symbol) + 1,
                    strBuffer.length());
        }
        return str;
    }


    public static String subString(String text, int length, String endWith) {
        int textLength = text.length();
        int byteLength = 0;
        StringBuffer returnStr = new StringBuffer();
        for (int i = 0; i < textLength && byteLength < length * 2; i++) {
            String str_i = text.substring(i, i + 1);
            if (str_i.getBytes().length == 1) {
                byteLength++;
            } else {
                byteLength += 2;
            }
            returnStr.append(str_i);
        }
        try {
            if (byteLength < text.getBytes("GBK").length) {
                returnStr.append(endWith);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnStr.toString();
    }


    /**
     */
    public static String encodeBase64File(String path) {
        String data = "";
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            data = Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    /**
     */
    public static Bitmap getBitmap(String fileName) {
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inTempStorage = new byte[12 * 1024];
        bfOptions.inPurgeable = true;
        bfOptions.inInputShareable = true;
        File file = new File(fileName);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        if (fs != null)
            try {
                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
                        bfOptions);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        return bmp;
    }

    public static String analysisStringBuffer(StringBuffer strBuffer,
                                              String symbol) {
        String str = "";
        if (!isEmpty(strBuffer.toString())) {
            str = strBuffer.toString().substring(0,
                    strBuffer.toString().lastIndexOf(symbol));
        }
        return str;
    }


    /**
     * 正方形
     *
     * @param layoutWh 调整Layout的宽高
     * @param w        屏幕宽度减去的值
     * @param b        倍数
     */
    public static void setLayoutWh(Activity activity, View layoutWh, int w, int b) {

        WindowManager wm1 = activity.getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();
        width = (width - CommonUtil.dip2px(activity, w)) / b;
        ViewGroup.LayoutParams para1 = layoutWh.getLayoutParams();
        para1.height = width;
        para1.width = width;
        layoutWh.setLayoutParams(para1);
    }

    /**
     * @param layoutWh 调整Layout的宽高
     * @param w        修改后的宽度
     * @param h        修改后的高度
     */
    public static void setLayoutWh(View layoutWh, int w, int h) {

        ViewGroup.LayoutParams para1 = layoutWh.getLayoutParams();
        para1.height = h;
        para1.width = w;
        layoutWh.setLayoutParams(para1);
    }


    public static String getdata2(String data, String data1) {
        String data2 = getCurrentDateTime2();
        data = data.replace(" ", "-");
        data1 = data1.replace(" ", "-");
        data2 = data2.replace(" ", "-");
        String[] data_nyr_sf = data.split("-"); // 2014 09 23 13
        String[] data1_nyr_sf = data1.split("-"); // 2014 09 23 13
        String[] data2_nyr_sf = data2.split("-"); // 2014 09 23 13

        int data_n = Integer.valueOf(data_nyr_sf[0]);
        int data_y = Integer.valueOf(data_nyr_sf[1]);
        int data_r = Integer.valueOf(data_nyr_sf[2]);
        int data_s = Integer.valueOf(data_nyr_sf[3]);
        int data_f = Integer.valueOf(data_nyr_sf[4]);

        int data1_n = Integer.valueOf(data1_nyr_sf[0]);
        int data1_y = Integer.valueOf(data1_nyr_sf[1]);
        int data1_r = Integer.valueOf(data1_nyr_sf[2]);
        int data1_s = Integer.valueOf(data1_nyr_sf[3]);
        int data1_f = Integer.valueOf(data1_nyr_sf[4]);

        String map = "";

        return map;

    }

    /*
     * 时间差
     */
    public static String getdata(String data2) {
        String time = null;

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            t.setToNow(); // 取得系统时间。
            String data1 = t.year + "-" + (t.month + 1) + "-" + t.monthDay
                    + " " + t.hour + ":" + t.minute + ":" + t.second + "";
            System.out.println(data1);
            Date now = df.parse(data1);
            Date date = df.parse(data2);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println("" + day + "天" + hour + "小时" + min + "分" + s
                    + "秒");

            if (day > 0) {
                time = day + "天前";

                return time;
            } else if (day < 1 && hour > 0) {
                time = hour + "小时前";

                return time;
            } else if (day < 1 && hour < 1 && min > 0) {
                time = min + "分钟前";

                return time;
            } else if (day < 1 && hour < 1 && min < 1 && s > 0) {
                time = s + "秒前";
                return time;
            }
        } catch (Exception e) {

        }

        return "";
    }

    public static String getstatus(String t_status) {
        String status = "";
        if (t_status.equals("W")) {
            status = "待支付";
        } else if (t_status.equals("P_S")) {
            status = "未确认";
        } else if (t_status.equals("S")) {
            status = "待发货";
        } else if (t_status.equals("H")) {
            status = "配送中";
        } else if (t_status.equals("C")) {
            status = "待评价";
        } else if (t_status.equals("D")) {
            status = "已取消";
        } else if (t_status.equals("E")) {
            status = "已过期";
        } else if (t_status.equals("B1") || t_status.equals("B3")) {
            status = "退款中";
        } else if (t_status.equals("B2") || t_status.equals("B4")) {
            status = "已退款";
        } else if (t_status.equals("COD")) {
            status = "到付";
        } else if (t_status.equals("ALED")) {
            status = "已完成";
        }
        return status;

    }

    public static String getconsume(String status, String evaluate) {
        String status_con = "";
        if (status.equals("consume")) {
//            status_con = "已消费";
            if (!evaluate.equals("N")) {

                status_con = "已完成";
            } else {

                status_con = "待评价";
            }
        } else if (status.equals("反消费")) {
            status_con = "反结账";
        } else if (status.equals("反存款")) {
            status_con = "反存款";
        } else if (status.equals("deposit")) {
            status_con = "存款";
        } else {
            status_con = "";
        }
        return status_con;

    }


    public static String getstatusonr(String t_status, String grouper_status) {


        String status = "";
        if (t_status.equals("S") && grouper_status.equals("N")) {
            status = "进行中";
        } else if (t_status.equals("W") && grouper_status.equals("W")) {
            status = "待支付";
        } else if (t_status.equals("D")) {
            status = "已取消";
        } else if (grouper_status.equals("E")) {
            status = "已过期";
        } else if (t_status.equals("S") && grouper_status.equals("C")) {
            status = "已完成";
        } else if (t_status.equals("S") && grouper_status.equals("N")) {
            status = "进行中";
        } else if (t_status.equals("S") && grouper_status.equals("F")) {
            status = "已失败";
        } else if (t_status.equals("B2") || t_status.equals("B4")) {
            status = "已退款";
        } else if (t_status.equals("B1") || t_status.equals("B3")) {
            status = "退款中";
        } else {
            status = "";
        }

        return status;

    }


    public static String getStatuSonrContxt(String t_status, String grouper_status) {
        String status = "";
        if (t_status.equals("S") && grouper_status.equals("N")) {
            status = "团购已付款，等待满团";
        } else if (t_status.equals("S") && grouper_status.equals("C")) {
            status = "团购成功";
        } else if (t_status.equals("W")) {
            status = "等待付款";
        } else if (t_status.equals("D")) {
            status = "订单已取消";
        } else if (t_status.equals("E")) {
            status = "订单已过期";
        } else if (t_status.equals("B1")) {
            status = "未发货退款处理中";
        } else if (t_status.equals("B2")) {
            status = "未发货退款成功";
        } else if (t_status.equals("B3")) {
            status = "退款处理中";
        } else if (t_status.equals("B4")) {
            status = "退款成功";
        } else if (t_status.equals("C")) {
            status = "待评价";
        } else if (t_status.equals("ALED")) {
            status = "已完成";
        } else {
            status = "";
        }

        return status;

    }


    public static String getorder_type(String t_status) {
        String status = "";
        if (t_status.equals("wx_office")) {
            status = "微官网订单（单买订单）";
        } else if (t_status.equals("pc_office")) {
            status = "PC官网订单";
        } else if (t_status.equals("gp")) {
            status = "团购订单";
        } else if (t_status.equals("join_gp")) {
            status = "参加团购订单";
        } else if (t_status.equals("select_order")) {
            status = "选购订单";
        }
        return status;

    }

    public static String getorder_typeone(String t_status) {
        String status = "";
        if (t_status.equals("order_wx_office")) {
            status = "微官网订单（单买订单）";
        } else if (t_status.equals("order_pc_office")) {
            status = "PC官网订单";
        } else if (t_status.equals("order_gp")) {
            status = "专享团购";
        } else if (t_status.equals("order_join_gp")) {
            status = "参加团购订单";
        } else if (t_status.equals("order_select_order")) {
            status = "选购订单";
        } else if (t_status.equals("order_foretaste_order")) {
            status = "试吃预售";
        }
        return status;

    }


    /**
     * @param status         订单状态
     * @param grouper_status 团购状态
     * @param isSelect       是否是试吃
     * @return order_type上标签   order_type_content底部语
     */
    public static Map<String, String> getorder_type_end(String status, String grouper_status, boolean isSelect) {
        Map<String, String> map = new HashMap<String, String>();
        String order_type = "";
        String order_type_content = "";
        if (status.equals("W") && grouper_status.equals("") && !isSelect) {
            order_type = "待支付";
            order_type_content = "等待付款";
        } else if (status.equals("P_S") && grouper_status.equals("") && !isSelect) {
            order_type = "未确认";
            order_type_content = "未确认";
        } else if (status.equals("H") && grouper_status.equals("") && !isSelect) {
            order_type = "配送中";
            order_type_content = "配送中";
        } else if (status.equals("C") && grouper_status.equals("") && !isSelect) {
            order_type = "待评价";
            order_type_content = "待评价";
        } else if (status.equals("ALED") && grouper_status.equals("") && !isSelect) {
            order_type = "已完成";
            order_type_content = "已完成";
        } else if (status.equals("D") && grouper_status.equals("") && !isSelect) {
            order_type = "已取消";
            order_type_content = "订单已取消";
        } else if (status.equals("E") && grouper_status.equals("") && !isSelect) {
            order_type = "已过期";
            order_type_content = "订单已过期";
        } else if (status.equals("B1") && grouper_status.equals("") && !isSelect) {
            order_type = "退款中";
            order_type_content = "正在退款中";
        } else if (status.equals("B3") && grouper_status.equals("") && !isSelect) {
            order_type = "退款中";
            order_type_content = "正在退款中";
        } else if (status.equals("B2") && grouper_status.equals("") && !isSelect) {
            order_type = "已退款";
            order_type_content = "未发货退款成功";
        } else if (status.equals("B4") && grouper_status.equals("") && !isSelect) {
            order_type = "已退款";
            order_type_content = "已发货退款成功";
        } else if (status.equals("S") && grouper_status.equals("") && !isSelect) {
            order_type = "待发货";
            order_type_content = "选购已付款，等待发货";
        } else if (status.equals("S") && !grouper_status.equals("") && grouper_status.equals("N") && !isSelect) {
            order_type = "进行中";
            order_type_content = "团购已付款，等待满团";
        } else if (status.equals("S") && !grouper_status.equals("") && grouper_status.equals("C") && !isSelect) {
            order_type = "团购成功";
            order_type_content = "已付款，待自提";
        } else if (status.equals("S") && !grouper_status.equals("") && grouper_status.equals("F") && !isSelect) {
            order_type = "团购失败";
            order_type_content = "团购失败";
        } else if (status.equals("W") && !grouper_status.equals("") && !isSelect) {
            order_type = "待支付";
            order_type_content = "等待付款";
        } else if (status.equals("D") && !grouper_status.equals("") && !isSelect) {
            order_type = "团购失败";
            order_type_content = "订单已取消";
        } else if (status.equals("E") && !grouper_status.equals("") && !isSelect) {
            order_type = "团购失败";
            order_type_content = "订单已过期";
        } else if (status.equals("B1") && !grouper_status.equals("") && !isSelect) {
            order_type = "退款中";
            order_type_content = "正在退款中";
        } else if (status.equals("B2") && !grouper_status.equals("") && !isSelect) {
            order_type = "团购失败";
            order_type_content = "未自提退款成功";
        } else if (status.equals("B2") && grouper_status.equals("E") && !isSelect) {
            order_type = "团购失败";
            order_type_content = "未成团退款成功";
        } else if (status.equals("B3") && !grouper_status.equals("") && !isSelect) {
            order_type = "退款中";
            order_type_content = "正在退款中";
        } else if (status.equals("B4") && !grouper_status.equals("") && !isSelect) {
            order_type = "团购失败";
            order_type_content = "已自提退款成功";
        } else if (status.equals("C") && !grouper_status.equals("") && !isSelect) {
            order_type = "待评价";
            order_type_content = "待评价";
        } else if (status.equals("ALED") && !grouper_status.equals("") && !isSelect) {
            order_type = "已完成";
            order_type_content = "已完成";
        } else if (status.equals("B5") && grouper_status.equals("") && isSelect) {
            order_type = "已过期";
            order_type_content = "已过期";
        } else if (status.equals("S") && grouper_status.equals("") && isSelect) {
            order_type = "已申请";
            order_type_content = "已申请，等待成功";
        } else if (status.equals("W") && grouper_status.equals("") && isSelect) {
            order_type = "待支付";
            order_type_content = "等待付款";
        } else if (status.equals("P_S") && grouper_status.equals("") && isSelect) {
            order_type = "未确认";
            order_type_content = "未确认";
        } else if (status.equals("H") && grouper_status.equals("") && isSelect) {
            order_type = "配送中";
            order_type_content = "配送中";
        } else if (status.equals("C") && grouper_status.equals("") && isSelect) {
            order_type = "待评价";
            order_type_content = "待评价";
        } else if (status.equals("ALED") && grouper_status.equals("") && isSelect) {
            order_type = "已完成";
            order_type_content = "已完成";
        } else if (status.equals("D") && grouper_status.equals("") && isSelect) {
            order_type = "已取消";
            order_type_content = "订单已取消";
        } else if (status.equals("E") && grouper_status.equals("") && isSelect) {
            order_type = "已过期";
            order_type_content = "订单已过期";
        } else if (status.equals("B1") && grouper_status.equals("") && isSelect) {
            order_type = "退款中";
            order_type_content = "正在退款中";
        } else if (status.equals("B3") && grouper_status.equals("") && isSelect) {
            order_type = "退款中";
            order_type_content = "正在退款中";
        } else if (status.equals("B2") && grouper_status.equals("") && isSelect) {
            order_type = "已退款";
            order_type_content = "未发货退款成功";
        } else if (status.equals("B4") && grouper_status.equals("") && isSelect) {
            order_type = "已退款";
            order_type_content = "已发货退款成功";
        }
        map.put("order_type", order_type);
        map.put("order_type_content", order_type_content);
        return map;
    }


    /**
     * 获取支付类型
     *
     * @param pay_type
     * @return
     */
    public static String getpay_type(String pay_type) {
        String data = "";

        if (pay_type.equals("wx_pay")) {
            data = "";
        } else if (pay_type.equals("wx_pay")) {
            data = "";
        }

        return data;
    }


    // 软键盘相关辅助类KeyBoardUtils

    /**
     * 打开软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {

        input = stringFilter(input);

        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 跟App相关的辅助类
     */

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 版本更新比较三个版本（配置时要注意但是最新版本不能小于最小版本）
     *
     * @param current_version 最新版本
     * @param min_version     最小版本
     * @return
     */
    public static int VersionComparison(Context context, String current_version, String min_version) {

//        状态   1：必须更新  2：可以不更新  ;0：不要更新
        int staet = 0;

//        最新版本
        String[] current_version_arrt = current_version.split("\\.");
//        最低要求版本
        String[] min_version_arrt = min_version.split("\\.");
//        当前版本
        String[] my_version_arrt = getVersionName(context).split("\\.");

        int size_1 = Math.max(my_version_arrt.length, min_version_arrt.length);
        int size = Math.max(current_version_arrt.length, size_1);
        for (int i = 0; i < size; i++) {

            staet = VersionComparison(i, current_version_arrt, min_version_arrt, my_version_arrt);

            if (staet != -1) {
                break;
            }

        }


        if (staet == -1) {
            staet = 0;
        }

        return staet;
    }

    /**
     * 版本更新比较三个版本（配置时要注意但是最新版本不能小于最小版本）
     *
     * @param current_version 最新版本
     * @param min_version     最小版本
     * @return
     */
    public static int VersionComparison(Context context, String current_version, String min_version, String my_version) {

//        状态   1：必须更新  2：可以不更新  ;0：不要更新
        int staet = 0;

//        最新版本
        String[] current_version_arrt = current_version.split("\\.");
//        最低要求版本
        String[] min_version_arrt = min_version.split("\\.");
//        当前版本
        String[] my_version_arrt = my_version.split("\\.");

        int size_1 = Math.max(my_version_arrt.length, min_version_arrt.length);
        int size = Math.max(current_version_arrt.length, size_1);
        for (int i = 0; i < size; i++) {
            staet = VersionComparison(i, current_version_arrt, min_version_arrt, my_version_arrt);
            if (staet != -1) {
                break;
            }
        }
        if (staet == -1) {
            staet = 0;
        }
        return staet;
    }

    /**
     * 辅助VersionComparison（）
     *
     * @param i
     * @param current_version_arrt
     * @param min_version_arrt
     * @param my_version_arrt
     * @return
     */
    private static int VersionComparison(int i, String[] current_version_arrt, String[] min_version_arrt, String[] my_version_arrt) {
//        状态   1：必须更新  2：可以不更新  ;0：不要更新
        int staet = -1;

        int int_current_version, int_min_version, int_my_version;


        if (current_version_arrt.length > i) {
            int_current_version = Integer.valueOf(current_version_arrt[i]);
        } else {
            int_current_version = 0;
        }
        if (min_version_arrt.length > i) {
            int_min_version = Integer.valueOf(min_version_arrt[i]);
        } else {
            int_min_version = 0;
        }
        if (my_version_arrt.length > i) {
            int_my_version = Integer.valueOf(my_version_arrt[i]);
        } else {
            int_my_version = 0;
        }


        if (int_my_version < int_current_version) {
            staet = 2;
            if (int_my_version < int_min_version) {
                staet = 1;
            }
            return staet;
        }


        if (int_my_version < int_min_version && int_my_version < int_current_version) {

            staet = 1;
        } else if (int_my_version >= int_min_version && int_my_version < int_current_version) {
            staet = 2;
        } else if (int_my_version > int_min_version && int_my_version > int_current_version) {
            staet = 0;
        } else if (int_my_version == int_min_version && int_my_version == int_current_version) {
            staet = -1;
        }


        return staet;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels - getStatusHeight(context);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 验证是否是手机号
     *
     * @param phone 手机号
     * @return false 则不是手机号 反之则是手机号
     */
    public static boolean isphone(String phone) {
        if (phone.length() == 11) {

            boolean m;

            Pattern p = Pattern.compile("^(13|14|15|17|18|19)\\d{9}$");

            m = p.matcher(phone).matches();
            return m;
        } else {
            return false;
        }

    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            return true;
        }

        FlymeSetStatusBarLightMode(window, dark);
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 是否具备打电话功能
     *
     * @param activity
     * @return
     */
    public static boolean isPad(Activity activity) {
        TelephonyManager telephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return true;
        } else {
            return false;
        }
    }

}

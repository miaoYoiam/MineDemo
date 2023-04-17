package com.example.minedemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.wasabeef.glide.transformations.internal.FastBlur;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class BitmapUtils {
    /**
     * @param bitmap
     * @param stretchWidth 需要拉伸宽度
     * @param marginRight  拉伸位置距离右边的距离
     * @return
     */
    public static native Bitmap stretch(Bitmap bitmap, int stretchWidth, int marginRight);

    /**
     * 两张图片合成
     *
     * @return
     */
    public static Bitmap conformBitmap(Bitmap background, Bitmap foreground, PorterDuff.Mode mode) {
        if (background == null) {
            return null;
        }

        Bitmap bmp = null;
        //下面这个Bitmap中创建的函数就可以创建一个空的Bitmap
        bmp = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmp);
        //首先绘制第一张图片，很简单，就是和方法中getDstImage一样
        canvas.drawBitmap(background, 0, 0, paint);

        //在绘制第二张图片的时候，我们需要指定一个Xfermode
        //这里采用Multiply模式，这个模式是将两张图片的对应的点的像素相乘
        //，再除以255，然后以新的像素来重新绘制显示合成后的图像
        paint.setXfermode(new PorterDuffXfermode(mode));

        Rect rectbg = new Rect(0, 0, background.getWidth(), background.getHeight());
        canvas.drawBitmap(foreground, null, rectbg, paint);

        canvas.save();//保存
        //store
        canvas.restore();//存储
        return bmp;
    }


    /**
     * 从Asserts下获取bitmap
     */
    public static Bitmap getFromAsserts(Context context, String assertName) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = context.getAssets().open(assertName);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return bitmap;
            }
        }
    }


    /**
     * convert Bitmap to byte array
     */
    public static byte[] bitmapToByte(Bitmap b) {
        ByteArrayOutputStream o = null;
        byte[] ret = null;
        try {
            o = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, o);
            ret = o.toByteArray();
        } catch (Exception e) {

        } finally {
            try {
                if (o != null) {
                    o.close();
                }
            } catch (IOException ignored) {

            }
        }
        return ret;
    }

    /**
     * convert byte array to Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {


        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * 把bitmap转换成Base64编码String
     */
    public static String bitmapToString(Bitmap bitmap) {
        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT);
    }

    /**
     * convert Drawable to Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * scale image
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     */
    public static Bitmap scaleImage(Bitmap src, float scaleWidth, float scaleHeight) {
        if (src == null) {
            return null;
        }
        try {
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * 生成bitmap缩略图
     *
     * @param bitmap
     * @param needRecycle 是否释放bitmap原图
     * @param newHeight   目标宽度
     * @param newWidth    目标高度
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitmap, boolean needRecycle, int newHeight, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (needRecycle)
            bitmap.recycle();
        return newBitMap;
    }

    /**
     * 保存Bitmap到文件
     *
     * @param bitmap
     * @param target
     */
    public static void saveBitmap(Bitmap bitmap, File target) {
        if (target.exists()) {
            target.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到文件
     *
     * @param bitmap
     * @param quality 保存质量 0..100
     * @param target
     */
    public static void saveBitmap(Bitmap bitmap, int quality, File target) {
        FileOutputStream out = null;
        try {
            if (target.exists()) {
                target.delete();
            }
            out = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

            }
        }
    }

    /**
     * 压缩bitmp到目标大小（质量压缩）
     *
     * @param bitmap
     * @param needRecycle
     * @param maxSize
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, boolean needRecycle, long maxSize) {
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream isBm = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;
            while (baos.toByteArray().length > maxSize) {
                baos.reset();//重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;//每次都减少10
            }
            isBm = new ByteArrayInputStream(baos.toByteArray());

            Bitmap bm = BitmapFactory.decodeStream(isBm, null, null);
            if (needRecycle) {
                bitmap.recycle();
            }
            bitmap = bm;
        } catch (Exception e) {
        } finally {
            try {
                if (isBm != null) {
                    isBm.close();
                }

            } catch (IOException e) {
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }
        }
        return bitmap;
    }

    /**
     * 压缩bitmp到目标大小（质量压缩）
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int quality) {
        if (bitmap == null) {
            return bitmap;
        }
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream isBm = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            if (Build.VERSION.SDK_INT < 21) {
                opts.inPurgeable = true;
            }
            opts.inSampleSize = 2;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            isBm = new ByteArrayInputStream(baos.toByteArray());
            Bitmap bm = BitmapFactory.decodeStream(isBm, null, opts);

            bitmap.recycle();
            bitmap = bm;

            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isBm != null) {
                    isBm.close();
                }
            } catch (IOException e) {
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }
        }
        return bitmap;
    }

    /**
     * 等比压缩（宽高等比缩放）
     *
     * @param bitmap
     * @param needRecycle
     * @param targetWidth
     * @param targeHeight
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, boolean needRecycle, int targetWidth, int targeHeight) {
        float sourceWidth = bitmap.getWidth();
        float sourceHeight = bitmap.getHeight();

        float scaleWidth = targetWidth / sourceWidth;
        float scaleHeight = targeHeight / sourceHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight); //长和宽放大缩小的比例
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (needRecycle) {
            bitmap.recycle();
        }
        bitmap = bm;
        return bitmap;
    }

    public static Bitmap compressBitmap(String imageFile, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        return compress(imageFile, null, false, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    private static Bitmap compress(String imageFile, String targetFile, boolean isSave, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options); //加载图片信息
        int sourceWidth = options.outWidth;
        int sourceHeight = options.outHeight;
        options.inJustDecodeBounds = false;
        //计算inSampleSize
        int inSampleSize = 1;
        //先根据宽度进行缩小
        while (sourceWidth / inSampleSize > targetWidth) {
            inSampleSize++;
        }
        //然后根据高度进行缩小
        while (sourceHeight / inSampleSize > targeHeight) {
            inSampleSize++;
        }

        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile, options);//加载真正bitmap

        bitmap = compressBitmap(bitmap, false, targetWidth, targeHeight); //等比缩放
        if (qualityCompress) {
            bitmap = compressBitmap(bitmap, true, maxSize); //压缩质量
        }

        if (isSave) {
            String savePath = imageFile;
            if (!TextUtils.isEmpty(targetFile)) {
                savePath = targetFile;
            }

            saveBitmap(bitmap, new File(savePath));//保存图片
        }

        return bitmap;
    }

    /**
     * 压缩某张图片(执行步骤sampleSize压缩->等比压缩->质量压缩)
     *
     * @param imageFile
     * @param targetFile      保存目标，为空表示源地址保存
     * @param qualityCompress 是否做质量压缩
     * @param maxSize         目标图片大小
     * @param targetWidth
     * @param targeHeight
     */
    public static void compressImage(String imageFile, String targetFile, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        Bitmap bitmap = compress(imageFile, targetFile, true, qualityCompress, maxSize, targetWidth, targeHeight);
        // bitmap.recycle();
    }

    public static void compressImage(String imageFile, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        compressImage(imageFile, null, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param targetWidth
     * @param targeHeight
     */
    public static void compressImage(String imageFile, int targetWidth, int targeHeight) {
        compressImage(imageFile, null, false, 0L, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param targetWidth
     * @param targeHeight
     * @return
     */
    public static Bitmap compressBitmap(String imageFile, int targetWidth, int targeHeight) {
        return compressBitmap(imageFile, false, 0L, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片缩小倍速
     */
    public static void compressImageSmall(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / scale;
        int targeHeight = options.outHeight / scale;
        compressImage(imageFile, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片缩小倍速
     * @return
     */
    public static Bitmap compressBitmapSmall(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / scale;
        int targeHeight = options.outHeight / scale;
        return compressBitmap(imageFile, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片放大倍速
     */
    public static void compressImageBig(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth * scale;
        int targeHeight = options.outHeight * scale;
        compressImage(imageFile, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片放大倍速
     * @return
     */
    public static Bitmap compressBitmapBig(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth * scale;
        int targeHeight = options.outHeight * scale;
        return compressBitmap(imageFile, targetWidth, targeHeight);
    }

    /**
     * 质量压缩图片
     *
     * @param imageFile
     * @param targetFile
     * @param qualityCompress
     * @param maxSize
     */
    public static void compressImage(String imageFile, String targetFile, boolean qualityCompress, long maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / 2;
        int targeHeight = options.outHeight / 2;
        compressImage(imageFile, targetFile, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    /**
     * 质量压缩图片
     *
     * @param imageFile
     * @param qualityCompress
     * @param maxSize
     */
    public static void compressImage(String imageFile, boolean qualityCompress, long maxSize) {
        compressImage(imageFile, null, qualityCompress, maxSize);
    }


    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degress     旋转角度
     * @param needRecycle
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress, boolean needRecycle) {
        Matrix m = new Matrix();
        m.postRotate(degress);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        if (needRecycle) {
            bitmap.recycle();
        }
        return bm;
    }

    /**
     * 根据path
     *
     * @param path
     * @return
     */
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap getRoundBitmap(Bitmap scaleBitmapImage) {
        if (scaleBitmapImage.getWidth() == 0 || scaleBitmapImage.getHeight() == 0) {
            return null;
        }

        int targetWidth = scaleBitmapImage.getWidth();
        int targetHeight = scaleBitmapImage.getHeight();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
                targetHeight), null);
        return targetBitmap;
    }

    public static Bitmap createCircleBitmap(Bitmap resource) {
        //获取图片的宽度
        int width = resource.getWidth();
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);

        //创建一个与原bitmap一样宽度的正方形bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //以该bitmap为低创建一块画布
        Canvas canvas = new Canvas(circleBitmap);
        //以（width/2, width/2）为圆心，width/2为半径画一个圆
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        //设置画笔为取交集模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //裁剪图片
        canvas.drawBitmap(resource, 0, 0, paint);
        return circleBitmap;
    }

    public static Bitmap createBlurBitmap(@NonNull Context context, @NonNull Bitmap toTransform, int radius, int sampling) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;
        Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

//        setCanvasBitmapDensity(toTransform, bitmap);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0, paint);
        bitmap = FastBlur.blur(bitmap, radius, true);
        return bitmap;
    }


    /**
     * 固定大小，固定路径。谨慎使用。
     **/
    public static String resizePicture(String srcPath) {
        if (srcPath == null || srcPath.isEmpty()) {
            return "";
        }
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);

        op.inJustDecodeBounds = false;
        int orginalWidth = op.outWidth;
        int orginalHeight = op.outHeight;

        if (orginalWidth <= 750 && orginalHeight <= 750) {
            return srcPath;
        }
        File inFile = new File(srcPath);
        File outFile = new File(srcPath.replace(inFile.getName(), "ex" + inFile.getName()));
        float aimHeight = 750f;
        float aimWidth = 750f;
        //数字越大,图片大小越小.
        float be = 1.0f;

        if (orginalWidth > orginalHeight && orginalWidth > aimWidth) {
            be = (orginalWidth / aimWidth);
        } else if (orginalWidth < orginalHeight && orginalHeight > aimHeight) {
            be = (orginalHeight / aimHeight);
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) aimWidth, (int) aimHeight, true);
        try {
            fos = new FileOutputStream(outFile);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {

            }
        }

        return outFile.getPath();
    }

    public static int[] getImageWH(String path) {
        int[] wh = {-1, -1};
        if (path == null) {
            return wh;
        }
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            InputStream is = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                is = new FileInputStream(path);
                BitmapFactory.decodeStream(is, null, options);
                wh[0] = options.outWidth;
                wh[1] = options.outHeight;
            } catch (Exception e) {
                Log.e("getImageWH Exception.%s", e.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {

                }
            }
        }
        return wh;
    }

    public static Bitmap createBitmapByScale(String path, int scale) {
        Bitmap bm = null;
        InputStream is = null;
        try {
            //获取宽高
            int[] wh = getImageWH(path);
            if (wh[0] == -1 || wh[1] == -1) {
                return null;
            }
            //读取图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(wh[0] / scale, wh[1] / scale);
            is = new FileInputStream(path);
            bm = BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            Log.e("createBitmapByScale Exception.%s", e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return bm;
    }


    public static Bitmap decodeResourceByStream(Resources resources, @DrawableRes int resourcesId) {
        return decodeResourceByStream(resources, resourcesId, 0, 0);
    }

    public static Bitmap decodeResourceByStream(Resources resources, @DrawableRes int resourcesId, int width, int height) {
        try {
            if (width <= 0 || height <= 0) {
                return BitmapFactory.decodeResource(resources, resourcesId);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, resourcesId, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
            if (inSampleSize < 1) {
                inSampleSize = 1;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeResource(resources, resourcesId, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeFile(String path) {
        return decodeFile(path, 0, 0);
    }

    public static Bitmap decodeFile(String path, int width, int height) {
        try {
            if (width <= 0 || height <= 0) {
                return BitmapFactory.decodeFile(path);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
            if (inSampleSize < 1) {
                inSampleSize = 1;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapByTarget(String path, int width, int height) {
        Bitmap bm = null;
        InputStream is = null;
        try {
            int[] wh = getImageWH(path);
            if (wh[0] == -1 || wh[1] == -1) {
                return null;
            }
            //读取图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(wh[0] / width, wh[1] / height);
            is = new FileInputStream(path);
            bm = BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return bm;
    }
}

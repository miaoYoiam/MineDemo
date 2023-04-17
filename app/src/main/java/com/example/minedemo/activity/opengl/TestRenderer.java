package com.example.minedemo.activity.opengl;

import static com.example.minedemo.activity.opengl.ShaderUtil.createProgram;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.minedemo.App;
import com.example.minedemo.R;
import com.example.minedemo.utils.BitmapUtils;
import com.example.minedemo.utils.DisplayUtilsKt;
import com.example.minedemo.utils.TextureUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Day：2022/6/20 11:24 上午
 *
 * @author zhanglei
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class TestRenderer extends GLRenderer {
    private static final String TAG = "TestRenderer";
    private int program;
    private int vPosition;
    private int fPosition;
    private int uColor;
    private int sTextureMap;

    private FloatBuffer vertices;
    private FloatBuffer vertices2;
    private FloatBuffer texturesBuffer;
    private FloatBuffer texturesBuffer2;

    private FloatBuffer getVertices(int width, int height) {
        float screenWidth = 1080.0f;
        float screenHeight = 1600.0f;
        float scaleW = width / screenWidth;
        float scaleH = height / screenHeight;
        float[] vertices = {
                -scaleW, -scaleH,//左下
                scaleW, -scaleH,//右下
                -scaleW, scaleH,//左上
                scaleW, scaleH //右上
        };

        // 创建顶点坐标数据缓冲
        // vertices.length*4是因为一个float占四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());             //设置字节顺序
        FloatBuffer vertexBuf = vbb.asFloatBuffer();    //转换为Float型缓冲
        vertexBuf.put(vertices);                        //向缓冲区中放入顶点坐标数据
        vertexBuf.position(0);                          //设置缓冲区起始位置

        return vertexBuf;
    }

    private FloatBuffer getTexture() {
        float[] vertices = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
        };
        // 创建片源坐标数据缓冲
        // vertices.length*4是因为一个float占四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());             //设置字节顺序
        FloatBuffer vertexBuf = vbb.asFloatBuffer();    //转换为Float型缓冲
        vertexBuf.put(vertices);                        //向缓冲区中放入顶点坐标数据
        vertexBuf.position(0);                          //设置缓冲区起始位置

        return vertexBuf;
    }

    float screenWidth = 1080.0f;
    float screenHeight = 1600.0f;

    float mScale = 0.5f;

    private FloatBuffer getVerticesV2(int width, int height) {
        float leftX = width / screenWidth;
        float startY = height / screenHeight;

        float scaleWidth = width * mScale;
        float rightX;
        if (scaleWidth == width / 2f) {
            rightX = 0f;
        } else if (scaleWidth > width / 2f) {
            rightX = (scaleWidth - (width / 2f)) / (screenWidth / 2);
        } else {
            rightX = -(((width / 2f) - scaleWidth) / (screenWidth / 2));
        }

        float[] vertices = {
                -leftX, -startY,
                rightX, -startY,
                -leftX, startY,
                rightX, startY
        };

        // 创建顶点坐标数据缓冲
        // vertices.length*4是因为一个float占四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());             //设置字节顺序
        FloatBuffer vertexBuf = vbb.asFloatBuffer();    //转换为Float型缓冲
        vertexBuf.put(vertices);                        //向缓冲区中放入顶点坐标数据
        vertexBuf.position(0);                          //设置缓冲区起始位置

        return vertexBuf;
    }

    private FloatBuffer getTexture2() {
        float[] vertices = {
                0.0f, 1.0f,
                mScale, 1.0f,
                0.0f, 0.0f,
                mScale, 0.0f,
        };
        // 创建片源坐标数据缓冲
        // vertices.length*4是因为一个float占四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());             //设置字节顺序
        FloatBuffer vertexBuf = vbb.asFloatBuffer();    //转换为Float型缓冲
        vertexBuf.put(vertices);                        //向缓冲区中放入顶点坐标数据
        vertexBuf.position(0);                          //设置缓冲区起始位置

        return vertexBuf;
    }

    @Override
    public void onCreated() {
        loadBitmap();
        vertices = getVertices(mBitmap.getWidth(), mBitmap.getHeight());
        vertices2 = getVerticesV2(mBitmap.getWidth(), mBitmap.getHeight());
        texturesBuffer = getTexture();
        texturesBuffer2 = getTexture2();

        //基于顶点着色器与片元着色器创建程序
        program = createProgram(verticesShader1, fragmentShader1);

        loadTextures();
        // 获取着色器中的属性引用id(传入的字符串就是我们着色器脚本中的属性名)
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
//        uColor = GLES20.glGetUniformLocation(program, "uColor");
//        sTextureMap = GLES20.glGetUniformLocation(program, "s_textureMap");

        fPosition = GLES20.glGetAttribLocation(program, "af_Position");
    }

    @Override
    public void onUpdate() {

    }

    private int[] texturesID = new int[1];
    private Bitmap mBitmap;

    private void loadBitmap() {
//        mBitmap = BitmapUtils.decodeResourceByStream(App.context.getResources(), R.mipmap.ic_launcher);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(DisplayUtilsKt.dp2px(App.context, 20));
        mBitmap = TextureUtil.createTextImage(paint, "测试绘制歌词渐变Hello world", Color.TRANSPARENT, 0);

    }


    @Override
    public void onDrawFrame(GLSurface outputSurface) {
//        // 设置clear color颜色RGBA(这里仅仅是设置清屏时GLES20.glClear()用的颜色值而不是执行清屏)
//        GLES20.glClearColor(1.0f, 0.0f, 0, 1.0f);
//
//        // 清除深度缓冲与颜色缓冲(清屏,否则会出现绘制之外的区域花屏)
//        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
//        // 使用某套shader程序
//        GLES20.glUseProgram(program);
//        // 为画笔指定顶点位置数据(vPosition)
//        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertices);
//        // 允许顶点位置数据数组
//        GLES20.glEnableVertexAttribArray(vPosition);
//        // 设置属性uColor(颜色 索引,R,G,B,A)
//        GLES20.glUniform4f(uColor, 0.0f, 1.0f, 1.0f, 1.0f);
//        // 绘制
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 3);


//
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 1, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glUseProgram(program);
        GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_textColor"), 0.7f, 0.4f, 0.2f);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturesID[0]);
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8, vertices);
        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8, texturesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(fPosition);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glUseProgram(program);
        GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_textColor"), 0.2f, 0.4f, 0.7f);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturesID[0]);
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8, vertices2);
        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8, texturesBuffer2);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(fPosition);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    private void loadTextures() {
        GLES20.glGenTextures(1, texturesID, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturesID[0]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        mBitmap.recycle();

    }

    @Override
    public void onDestroy() {

    }

    // 顶点着色器的脚本
    private static final String verticesShader1
            = "attribute vec4 vPosition;\n" // 顶点位置属性vPosition
            + "attribute vec2 af_Position;\n" //
            + "varying vec2 v_texCoord;\n" //
            + "void main(){                         \n"
            + "   v_texCoord = af_Position;\n" //
            + "   gl_Position = vPosition;\n" // 确定顶点位置
            + "}";

    private static final String fragmentShader1
            = "precision mediump float;         \n" // 声明float类型的精度为中等(精度越高越耗资源)
            + "varying vec2 v_texCoord;             \n" //
            + "uniform sampler2D sTexture;             \n" //
            + "uniform vec3 u_textColor;           \n" //
            + "void main(){                     \n"
            + "   vec4 color = texture2D(sTexture,v_texCoord);                    \n"
            + "   gl_FragColor =vec4(u_textColor , 1.0f) * color;    \n" //
            + "}";

}

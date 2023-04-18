package com.example.minedemo.asm;

import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Create by cxzheng on 2019/8/26
 * 全方法插桩内容
 */
public class AsmRootDelegate {

    public static List<Entity> methodList = new LinkedList<>();

    public static void startTrace(String name) {
        Trace.beginSection(name);
        synchronized (methodList) {
            methodList.add(new Entity(name, System.currentTimeMillis(), true, isInMainThread()));
        }
    }

    public static void endTrace(String name) {
        Trace.endSection();
        synchronized (methodList) {
            methodList.add(new Entity(name, System.currentTimeMillis(), false, isInMainThread()));
        }
    }

    public static void logger(String name) {
        Log.i("TraceMan", String.format("=====> test logger method name: %s", name));
    }


    public static void startCollectMethodCost() {
        resetTraceManData();
    }

    public static List<MethodInfo> endCollectMethodCost() {
        List<MethodInfo> resultList = obtainMethodCostData();
        resetTraceManData();
        return resultList;
    }


    public static void resetTraceManData() {
        synchronized (methodList) {
            methodList.clear();
        }
    }

    /**
     * 处理插桩数据，按顺序获取所有方法耗时
     */
    public static List<MethodInfo> obtainMethodCostData() {
        synchronized (methodList) {
            List<MethodInfo> resultList = new ArrayList();
            for (int i = 0; i < methodList.size(); i++) {
                Entity startEntity = methodList.get(i);
                if (!startEntity.isStart) {
                    continue;
                }
                startEntity.pos = i;
                Entity endEntity = findEndEntity(startEntity.name, i + 1);

                if (startEntity != null && endEntity != null && endEntity.time - startEntity.time > 0) {
                    resultList.add(createMethodInfo(startEntity, endEntity));
                }
            }
            return resultList;
        }
    }

    /**
     * 找到方法对应的结束点
     *
     * @param name
     * @param startPos
     * @return
     */
    private static Entity findEndEntity(String name, int startPos) {
        int sameCount = 1;
        for (int i = startPos; i < methodList.size(); i++) {
            Entity endEntity = methodList.get(i);
            if (endEntity.name.equals(name)) {
                if (endEntity.isStart) {
                    sameCount++;
                } else {
                    sameCount--;
                }
                if (sameCount == 0 && !endEntity.isStart) {
                    endEntity.pos = i;
                    return endEntity;
                }
            }
        }
        return null;
    }

    private static MethodInfo createMethodInfo(Entity startEntity, Entity endEntity) {
        return new MethodInfo(startEntity.name,
                endEntity.time - startEntity.time, startEntity.pos, endEntity.pos, startEntity.isMainThread);
    }


    private static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    static class Entity {
        public String name;
        public Long time;
        public boolean isStart;
        public int pos;
        public boolean isMainThread;

        public Entity(String name, Long time, boolean isStart, boolean isMainThread) {
            this.name = name;
            this.time = time;
            this.isStart = isStart;
            this.isMainThread = isMainThread;
        }
    }

}

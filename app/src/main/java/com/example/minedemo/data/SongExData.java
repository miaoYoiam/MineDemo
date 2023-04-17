package com.example.minedemo.data;

/**
 * @author WeChat@fredzzt
 * @Description
 * @date 2019-09-06
 */
public class SongExData {
    public int vibratoTotal = 0;
    public int vibratoMatch = 0;
    public int mordentTotal = 0;
    public int mordentMatch = 0;
    public int glissandoTotal = 0;
    public int glissandoMatch = 0;

    public void clear() {
        vibratoTotal = 0;
        vibratoMatch = 0;
        mordentTotal = 0;
        mordentMatch = 0;
        glissandoTotal = 0;
        glissandoMatch = 0;
    }
}

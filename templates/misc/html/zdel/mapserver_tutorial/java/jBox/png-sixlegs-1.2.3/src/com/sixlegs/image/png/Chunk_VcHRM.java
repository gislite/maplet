// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

final class Chunk_VcHRM
extends Chunk_cHRM
{
    Chunk_VcHRM(PngImage img)
    {
        this.img = img;
        xw = 31270;
        yw = 32900;
        xr = 64000;
        yr = 33000;
        xg = 30000;
        yg = 60000;
        xb = 15000;
        yb =  6000;
        calc();
    }
}

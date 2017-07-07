// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class PixelInputStream
{
    final private BitMover mover;
    final private InputStream str;
    final private int[] leftover = new int[8];
    private int leftamt = 0;

    /* package */ final int fillSize;

    PixelInputStream(PngImage img, InputStream str)
    throws PngException
    {
        this.str = str;
        fillSize = Math.max(1, 8 / img.data.header.depth);
        mover = BitMover.getBitMover(img);
    }

    int read(int b[], int off, int len)
    throws IOException
    {
        int needed = len;
        int total = len;
        if (leftamt > 0) {
            int fromleft = (needed > leftamt ? leftamt : needed);
            System.arraycopy(leftover, 8 - leftamt, b, off, fromleft);
            needed -= fromleft;
            leftamt -= fromleft;
        }
        if (needed > 0) {
            off = mover.fill(b, str, off, needed / fillSize);
            needed %= fillSize;
            if (needed > 0) {
                leftamt = fillSize - needed;
                mover.fill(leftover, str, 8 - fillSize, 1);
                System.arraycopy(leftover, 8 - fillSize, b, off, needed);
            }
        }
        return total;
    }
}

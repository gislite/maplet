// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover4G
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int x = str.read();
            int p1 = x >>> 4 & 0xF, p2 = x & 0xF;
            pixels[off++] = (p1 == transgray ? 0 : 0xFF000000) | gammaTable[p1];
            pixels[off++] = (p1 == transgray ? 0 : 0xFF000000) | gammaTable[p2];
        }
        return off;
    }
}

// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover16GA
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int hi = str.read();
            str.read();
            pixels[off++] = gammaTable[hi] | (str.read() << 24);
            str.read();
        }
        return off;
    }
}

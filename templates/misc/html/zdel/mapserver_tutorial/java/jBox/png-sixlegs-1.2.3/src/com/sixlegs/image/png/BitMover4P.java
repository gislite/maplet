// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover4P
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int x = str.read();
            pixels[off++] = x >>> 4 & 0x0F;
            pixels[off++] = x & 0x0F;
        }
        return off;
    }
}

// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover2G
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int x = str.read();
            int p1 = x >>> 6 & 0x03, p2 = x >>> 4 & 0x03, p3 = x >>> 2 & 0x03, p4 = x & 0x03;
            pixels[off++] = (p1 == transgray ? 0 : 0xF0000000) | gammaTable[p1];
            pixels[off++] = (p2 == transgray ? 0 : 0xF0000000) | gammaTable[p2];
            pixels[off++] = (p3 == transgray ? 0 : 0xF0000000) | gammaTable[p3];
            pixels[off++] = (p4 == transgray ? 0 : 0xF0000000) | gammaTable[p4];
        }
        return off;
    }
}

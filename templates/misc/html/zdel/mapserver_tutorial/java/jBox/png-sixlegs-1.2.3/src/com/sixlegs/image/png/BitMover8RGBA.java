// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover8RGBA
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int r = gammaTable[str.read()];
            int g = gammaTable[str.read()];
            int b = gammaTable[str.read()];
            pixels[off++] = r << 16 | g << 8 | b | str.read() << 24;
        }
        return off;
    }
}

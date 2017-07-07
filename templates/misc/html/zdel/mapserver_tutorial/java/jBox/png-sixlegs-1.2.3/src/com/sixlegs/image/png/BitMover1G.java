// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover1G
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int x = str.read();
            for (int i = 7; i >= 0; i--) {
                int p1 = (x >>> i & 1);
                pixels[off++] = (p1 == transgray ? 0 : 0xFF000000) | gammaTable[p1];
            }
        }
        return off;
    }
}

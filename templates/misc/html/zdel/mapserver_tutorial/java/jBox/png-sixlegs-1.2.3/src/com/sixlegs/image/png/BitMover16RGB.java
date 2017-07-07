// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;

final class BitMover16RGB
extends BitMover
{
    int fill(int[] pixels, InputStream str, int off, int len)
    throws IOException
    {
        for (int n = 0; n < len; n++) {
            int rhi = str.read();
            int rlo = str.read();
            int ghi = str.read();
            int glo = str.read();
            int bhi = str.read();
            int blo = str.read();
            int chkhi = rhi << 16 | ghi << 8 | bhi;
            int chklo = rlo << 16 | glo << 8 | blo;
            int val = gammaTable[rhi] << 16 | gammaTable[ghi] << 8 | gammaTable[bhi];
            pixels[off++] = (trans == chkhi && translow == chklo ? 0 : 0xFF000000) | val;
        }
        return off;
    }
}

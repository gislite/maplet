// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

final class CRCInputStream
extends FilterInputStream
{
    private CRC32 crc = new CRC32();
    private int byteCount = 0;

    public CRCInputStream(InputStream in)
    {
        super(in);
    }

    public long getValue()
    {
        return (long)crc.getValue();
    }

    public void reset()
    {
        byteCount = 0;
        crc.reset();
    }

    public int count()
    {
        return byteCount;
    }

    public int read()
    throws IOException
    {
        int x = in.read();
        if (x != -1) {
            crc.update(x);
            byteCount++;
        }
        return x;
    }

    public int read(byte[] b, int off, int len)
    throws IOException
    {
        int x = in.read(b, off, len);
        if (x != -1) {
            crc.update(b, off, x);
            byteCount += x;
        }
        return x;
    }

    private byte[] byteArray = new byte[0];

    public long skip(long n)
    throws IOException
    {
        // TODO: what if n > Integer.MAX_VALUE ?
        if (byteArray.length < n) byteArray = new byte[(int)n];
        return read(byteArray, 0, (int)n);
    }
}
  

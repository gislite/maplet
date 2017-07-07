// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

final class UnfilterInputStream
extends InputStream
{
    final private Chunk_IHDR header;
    final private int rowSize;
    final private int bpp;

    final private InflaterInputStream infstr;
    final private byte[] prev;
    final private byte[] cur;

    private int nextPass;
    private int rowsLeftInPass;
    private int bytesPerRow;
    private int pullSize;
    private int xc, xp, xPtr;

    UnfilterInputStream(PngImage img, InputStream s)
    {
        header = img.data.header;
        infstr = new InflaterInputStream(s, new Inflater(), PngImage.BUFFER_SIZE);
        bpp = Math.max(1, header.depth * header.samples / 8);
        int maxPassWidth = header.interlacer.getMaxPassWidth();
        int maxBytesPerRow = getByteWidth(maxPassWidth);
        rowSize = maxBytesPerRow + bpp;
        prev = new byte[rowSize];
        cur = new byte[rowSize];
        for (int i = 0; i < rowSize; i++) cur[i] = 0;
    }

    private int getByteWidth(int pixels)
    {
        if (header.samples == 1) {
            int dppb = 16 / header.depth; // == 2 * pixels-per-byte
            int w2 = pixels * 2;
            return (w2 % dppb == 0 ? w2 / dppb : (w2 + dppb - (w2 % dppb)) / dppb);
        } else {
            return pixels * header.samples * header.depth / 8;
        }
    }

    private int readRow ()
    throws IOException
    {
        if (rowsLeftInPass == 0) {
            while (rowsLeftInPass == 0 || bytesPerRow == 0) {
                if (nextPass >= header.interlacer.numPasses()) return -1;
                rowsLeftInPass = header.interlacer.getPassHeight(nextPass);
                bytesPerRow = getByteWidth(header.interlacer.getPassWidth(nextPass));
                nextPass++;
            }
            pullSize = bytesPerRow + bpp;
            for (int i = 0; i < pullSize; i++) prev[i] = 0;
        }
        rowsLeftInPass--;

        int filterType = infstr.read();
        if (filterType == -1)
            return -1;
        if (filterType > 4 || filterType < 0) 
            throw new PngException("Bad filter type: " + filterType);
        int needed = bytesPerRow;
        while (needed > 0) {
            int r = infstr.read(cur, bytesPerRow - needed + bpp, needed);
            if (r == -1) return -1;
            needed -= r;
        }

        // TODO: add support for FILTER_TYPE_INTRAPIXEL

        switch (filterType) {
        case 0: // None
            break;
        case 1: // Sub
            for (xc = bpp, xp = 0; xc < rowSize; xc++, xp++) {
                cur[xc] = (byte)(cur[xc] + cur[xp]);
            }
            break;
        case 2: // Up
            for (xc = bpp, xp = 0; xc < rowSize; xc++, xp++) {
                cur[xc] = (byte)(cur[xc] + prev[xc]);
            }
            break;
        case 3: // Average
            for (xc = bpp, xp = 0; xc < rowSize; xc++, xp++) {
                cur[xc] = (byte)(cur[xc] + ((0xFF & cur[xp]) + (0xFF & prev[xc])) / 2);
            }
            break;
        case 4: // Paeth
            for (xc = bpp, xp = 0; xc < rowSize; xc++, xp++) {
                cur[xc] = (byte)(cur[xc] + Paeth(cur[xp], prev[xc], prev[xp]));
            }
            break;
        default:
            throw new PngException("unrecognized filter type " + filterType);
        }

        System.arraycopy(cur, 0, prev, 0, rowSize);
        return 0;
    }

    private int Paeth(byte L, byte u, byte nw)
    {
        int a = 0xFF & L; //  inline byte->int
        int b = 0xFF & u; 
        int c = 0xFF & nw; 
        int p = a + b - c;
        int pa = p - a; if (pa < 0) pa = -pa; // inline Math.abs
        int pb = p - b; if (pb < 0) pb = -pb; 
        int pc = p - c; if (pc < 0) pc = -pc; 
        if (pa <= pb && pa <= pc) return a;
        if (pb <= pc) return b;
        return c;
    }

    private byte[] _b = new byte[1];

    public int read()
    throws IOException
    {
        return read(_b, 0, 1) > 0 ? _b[0] & 0xff : -1;
    }

    public int read(byte[] b, int off, int len)
    throws IOException
    {
        int count = 0;
		while (len > 0) {
            if (xPtr == 0) {
                if (readRow() == -1) return ( count == 0 ? -1 : count );
                xPtr = bpp;
            }
            int L = Math.min(len, pullSize - xPtr);
            System.arraycopy(cur, xPtr, b, off, L);
            count += L;
            xPtr = (xPtr + L) % pullSize;
            off += L;
            len -= L;
        }
        return count;
    }

    public void close()
    throws IOException
    {
        infstr.close();
    }
}

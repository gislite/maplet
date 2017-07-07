// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;

class Chunk
implements Cloneable
{
    /* package */ int length;
    /* package */ int type;

    protected PngImage img;
    protected ExDataInputStream in_data;

    Chunk(int type)
    {
        this.type = type;
    }

    Chunk copy()
    {
        try {
            return (Chunk)clone();
        } catch (CloneNotSupportedException e) { 
            return null;
        }
    }

    boolean isAncillary()
    {
        return ((type & 0x20000000) != 0);
    }

    final boolean isPrivate ()
    {
        return ((type & 0x00200000) != 0);
    }

    final boolean isReservedSet ()
    {
        return ((type & 0x00002000) != 0);
    }

    final boolean isSafeToCopy ()
    {
        return ((type & 0x00000020) != 0);
    }

    final boolean isUnknown ()
    {
        return getClass() == Chunk.class;
    }

    int bytesRemaining()
    {
        return Math.max(0, length + 4 - img.data.in_idat.count());
    }

    protected boolean multipleOK() { return true; }
    protected boolean beforeIDAT() { return false; }
  
    static String typeToString(int x)
    {
        return ("" + 
                (char)((x >>> 24) & 0xFF) + 
                (char)((x >>> 16) & 0xFF) + 
                (char)((x >>>  8) & 0xFF) + 
                (char)((x       ) & 0xFF));
    }

    static int stringToType(String id)
    {
        return ((((int)id.charAt(0) & 0xFF) << 24) | 
                (((int)id.charAt(1) & 0xFF) << 16) | 
                (((int)id.charAt(2) & 0xFF) <<  8) | 
                (((int)id.charAt(3) & 0xFF)      ));
    }

    final void badLength(int correct)
    throws PngException
    {
        throw new PngException("Bad " + typeToString(type) +
                               " chunk length: " + in_data.unsign(length) +
                               " (expected " + correct + ")");
    }

    final void badLength()
    throws PngException
    {
        throw new PngException("Bad " + typeToString(type) +
                               " chunk length: " + in_data.unsign(length));
    }

    protected void readData()
    throws IOException
    {
        in_data.skipBytes(length);
    }

    static final int IHDR = 0x49484452;
    static final int PLTE = 0x504c5445;
    static final int IDAT = 0x49444154;
    static final int IEND = 0x49454e44;
    static final int bKGD = 0x624b4744;
    static final int cHRM = 0x6348524d;
    static final int gAMA = 0x67414d41;
    static final int hIST = 0x68495354;
    static final int pHYs = 0x70485973;
    static final int sBIT = 0x73424954;
    static final int tEXt = 0x74455874;
    static final int tIME = 0x74494d45;
    static final int tRNS = 0x74524e53;
    static final int zTXt = 0x7a545874;
    static final int sRGB = 0x73524742;
    static final int sPLT = 0x73504c54;
    static final int oFFs = 0x6f464673;
    static final int sCAL = 0x7343414c;
    static final int iCCP = 0x69434350;
    static final int pCAL = 0x7043414c;
    static final int iTXt = 0x69545874;
    static final int gIFg = 0x67494667;
    static final int gIFx = 0x67494678;
}

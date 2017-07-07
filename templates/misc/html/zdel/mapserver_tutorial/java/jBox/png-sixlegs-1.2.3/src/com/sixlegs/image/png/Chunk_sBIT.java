// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_sBIT
extends Chunk
{
    Chunk_sBIT()
    {
        super(sBIT);
    }

    protected boolean multipleOK()
    {
        return false;
    }

    protected boolean beforeIDAT()
    {
        return true;
    }

    protected void readData()
    throws IOException
    {
        byte[] sbit = null;
        if (img.data.palette != null)
            throw new PngException("sBIT chunk must precede PLTE chunk");
        int compare_depth = img.data.header.depth;
        switch (img.data.header.colorType) {
        case PngImage.COLOR_TYPE_GRAY:
            sbit = new byte[3];
            sbit[0] = sbit[1] = sbit[2] = in_data.readByte(); 
            break;
        case PngImage.COLOR_TYPE_PALETTE:
            compare_depth = 8;
            /* fall through */
        case PngImage.COLOR_TYPE_RGB:
            sbit = new byte[3];
            sbit[0] = in_data.readByte();
            sbit[1] = in_data.readByte();
            sbit[2] = in_data.readByte();
            break;
        case PngImage.COLOR_TYPE_GRAY_ALPHA:
            sbit = new byte[4];
            sbit[0] = sbit[1] = sbit[2] = in_data.readByte();
            sbit[3] = in_data.readByte();
            break;
        case PngImage.COLOR_TYPE_RGB_ALPHA:
            sbit = new byte[4];
            sbit[0] = in_data.readByte();
            sbit[1] = in_data.readByte();
            sbit[2] = in_data.readByte();
            sbit[3] = in_data.readByte();
            break;
        }
        for (int i = 0; i < sbit.length; i++) {
            if (sbit[i] <= 0 || sbit[i] > compare_depth)
                throw new PngExceptionSoft("Illegal sBIT component depth");
        }

        img.data.properties.put("significant bits", sbit);
    }
}

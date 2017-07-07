// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_pHYs
extends Chunk
{
    Chunk_pHYs()
    {
        super(pHYs);
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
        long pixelsPerUnitX = in_data.readUnsignedInt();
        long pixelsPerUnitY = in_data.readUnsignedInt();
        int unit = in_data.readUnsignedByte();
        if (unit != PngImage.UNIT_UNKNOWN && unit != PngImage.UNIT_METER)
            throw new PngExceptionSoft("Illegal pHYs chunk unit specifier: " + unit);

        img.data.properties.put("pixel dimensions x", new Long(pixelsPerUnitX));
        img.data.properties.put("pixel dimensions y", new Long(pixelsPerUnitY));
        img.data.properties.put("pixel dimensions unit", new Integer(unit));
    }    
}

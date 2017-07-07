// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_oFFs
extends Chunk
{
    Chunk_oFFs ()
    {
        super(oFFs);
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
        int imagePositionX = in_data.readInt();
        int imagePositionY = in_data.readInt();
        int unit = in_data.readUnsignedByte();
        if (unit != PngImage.UNIT_PIXEL && unit != PngImage.UNIT_MICROMETER)
            throw new PngExceptionSoft("Illegal oFFs chunk unit specifier: " + unit);

        img.data.properties.put("image position x", new Integer(imagePositionX));
        img.data.properties.put("image position y", new Integer(imagePositionY));
        img.data.properties.put("image position unit", new Integer(unit));
    }    
}
